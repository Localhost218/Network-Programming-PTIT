# Role & Objective
You are an expert Backend Systems Engineer & DevOps Architect. Build a self-hosted, zero-auth **Network Programming Online Judge** for Java Network Programming exercises (PTIT Network Programming / DBPTIT style: TCP Sockets, UDP Sockets, Java RMI, and SOAP/JAX-WS Web Services).

**Core Priorities:**
1. **The Judge Engine is the #1 priority:** It must reliably compile student Java client submissions, spin up an isolated reference Judge Server for the specific problem, execute the client-server network interaction, validate the protocol/data exchange, and return accurate verdicts (`AC`, `WA`, `TLE`, `RE`, `CE`) with actionable debug logs.
2. **Zero Auth / Zero Bloat:** No login, no user accounts, no admin dashboard. Open the app -> see problems -> submit code -> get verdict.
3. **Clean, Self-Designed UI:** Build a clean, developer-friendly split-pane UI (do NOT spend time scraping or crawling any external website like `db.ptit.edu.vn`).

# Local Dataset
- **Workspace Root / Dataset Directory:** `/home/ngtukien/Projects/Network-Programming-PTIT`
- **Problem Specifications PDF:** `./Bai-tap-Lap-trinh-mang-Hungdn-2024.pdf`
- **Reference Client Solutions & Shared Models/Interfaces:**
  - `./TCP/` — TCP client solutions (`TCP_*.java`) and shared `Serializable` models (`Laptop.java`, `Customer.java`, etc. in `package TCP;`).
  - `./UDP/` — UDP client solutions (`UDP_*.java`) and shared `Serializable` models (`Book.java`, `Customer.java`, `Employee.java`, `PricedProduct.java`, `Product.java`, `Student.java` in `package UDP;`).
  - `./RMI/` — RMI client solutions (`RMI_*.java`), RMI remote interfaces (`ByteService.java`, `CharacterService.java`, `DataService.java`, `ObjectService.java`), and shared `Serializable` models (`Book.java`, `BookX.java`, `Employee.java`, `Event.java`, `Order.java`, `Student.java`, `Ticket.java`, `TicketSla.java` in `package RMI;`).
  - `./WS/` and `./SOAP/` — JAX-WS / SOAP client solutions (`WS_*.java`, `SOAP_*.java`, e.g., calling `DataService`, `CharacterService`, `ObjectService` ports). Note: `./WS/` and `./SOAP/` contain overlapping problems—deduplicate them cleanly during seeding.

---

# Step-by-Step Execution Plan

## Phase 1: Inspect Local Dataset & Design Judge Adapters
1. Immediately inspect `/home/ngtukien/Projects/Network-Programming-PTIT`:
   - Extract and parse problem descriptions from `./Bai-tap-Lap-trinh-mang-Hungdn-2024.pdf` (using `pypdf`/`pdfplumber`/`pdftotext`) and match them to the reference `.java` files in `./TCP`, `./UDP`, `./RMI`, `./WS`, and `./SOAP`.
   - Analyze how `package` declarations, `HOST`, `PORT`, `studentCode`, and `qCode` are used in the reference clients (e.g., sending `"studentCode;qCode"` or `";studentCode;qCode"` first, or passing `(studentCode, qCode)` to RMI/SOAP methods).
   - Note that reference clients often hardcode `HOST` (e.g. `"36.50.135.242"`) and `PORT` (e.g. `2206`–`2209` or `1099`). Design a clean way to rewrite/override `HOST`/`PORT` (or inject via environment/regex/AST/socket redirect or standardized `localhost:<port>`) when running submissions against the isolated Judge Server.
2. Design a **Unified Judge Runner Architecture** (in Python FastAPI or Node.js/TypeScript + Docker/subprocess isolation) that supports all 4 protocols cleanly:
   - **TCP (`Socket` / `ServerSocket`):**
     - Supports `InputStream`/`OutputStream` (byte arrays), `DataInputStream`/`DataOutputStream`, `BufferedReader`/`BufferedWriter`, and `ObjectInputStream`/`ObjectOutputStream` (ensure shared serializable model classes like `TCP.Laptop`, `TCP.Customer` are compiled onto the classpath of both Judge Server and Student Client!).
   - **UDP (`DatagramSocket` / `DatagramPacket`):**
     - Handles packet framing (`requestId` + payload bytes/serialized objects), packet loss timeout, and response verification.
   - **Java RMI (`java.rmi.registry.LocateRegistry`):**
     - Spins up an isolated programmatic `LocateRegistry.createRegistry(port)` on a free port, binds the reference remote object (`RMIBaseService` / `RMIDataService` / `RMICharacterService` / `RMIObjectService` / `RMIByteService`), compiles shared RMI interfaces into both classpaths, and runs the student's RMI client lookup + method invocations.
   - **Web Services (JAX-WS / SOAP):**
     - Publishes the reference `@WebService` endpoint (e.g., via `Endpoint.publish(...)` in Java with JAX-WS runtime dependencies) on a dynamic port, serves the WSDL, generates/provides the client stubs (`wsimport` or pre-compiled service/port classes in `shared/`), runs the student client against it, and verifies the submitted answer via the service's `submit*` method.

## Phase 2: Build the Backend & Isolated Judge Pipeline
Implement the backend with **SQLite** (zero setup) and a sequential/queued **Judge Worker**:
1. **Workspace Isolation per Submission:**
   - Create a temporary sandbox directory `/tmp/judge_<submission_id>/` with separate subdirectories: `server/`, `client/`, and `shared/` (for DTOs, RMI interfaces, and JAX-WS client stubs).
   - Ensure student code cannot read the `server/` directory or tamper with reference test cases.
2. **Port & Process Lifecycle Management (Critical):**
   - Allocate a free ephemeral port dynamically per judge run so concurrent or back-to-back submissions never hit `Address already in use` (`BindException`).
   - Patch/inject the target `host` (`127.0.0.1`) and allocated `port` cleanly into the client execution (or rewrite hardcoded remote IPs/ports before compilation while preserving line numbers where possible, and also document `127.0.0.1:<port>` in the UI).
   - Enforce a strict execution timeout (e.g., **5 seconds**) and process-group kill (`SIGKILL` on the entire process tree) so hung sockets never leak zombie Java processes.
3. **Verdict & Grading Logic:**
   - **`CE` (Compilation Error):** `javac` returns non-zero -> return exact compiler `stderr`.
   - **`TLE` (Time Limit Exceeded):** Client or socket handshake exceeds 5s -> kill processes and report where it stalled (e.g., *"Timed out waiting for client connection"* vs *"Client connected but did not send final response"*).
   - **`RE` (Runtime Error):** Non-zero exit code or unhandled Java exception (`ClassNotFoundException`, `StreamCorruptedException`, `EOFException`) -> return sanitized stack trace.
   - **`WA` (Wrong Answer):** Protocol completed, but final answer or sequence did not match expected output -> return clear diff/feedback from the Judge Server (e.g., `Expected: "abc", Received: "cba"`).
   - **`AC` (Accepted):** All randomized test rounds passed -> record execution time (ms) and update problem status to `Solved`.
4. **Auto-Seeder (`scripts/seed.py` or `.ts`):**
   - Parse `./Bai-tap-Lap-trinh-mang-Hungdn-2024.pdf` and scan `./TCP`, `./UDP`, `./RMI`, `./WS`, `./SOAP`.
   - Auto-detect protocol (`TCP`, `UDP`, `RMI`, `WS`), deduplicate `WS`/`SOAP`, extract shared model/interface classes, generate or wrap the Judge Server harness from the reference solution, and populate SQLite.

## Phase 3: Build a Clean, Practical Frontend
Build a fast React (Vite + TypeScript + Tailwind CSS + Monaco Editor) frontend:
- **Problem List View:** Filter by protocol (`All`, `TCP`, `UDP`, `RMI`, `WS`), search by code/title, show `Solved` / `Attempted` status.
- **Exercise Workspace (Split-Pane):**
  - **Left Pane:** Problem title, protocol badge, connection parameters (`Host`, `Port`, required `qCode`, shared class definitions / RMI interface signatures if any), and full problem statement.
  - **Right Pane:** Monaco Java Editor (supports pasting code or uploading `.java` / `.zip` multi-file solutions) + **Submit** button.
  - **Bottom/Drawer Result Panel:** Real-time polling (`Compiling...` -> `Running Server & Client...` -> `AC/WA/TLE/CE/RE`) showing execution time and step-by-step protocol logs from the Judge Server.

## Phase 4: Verification
1. Provide a single command to start everything (`docker compose up --build` or `./start.sh` with JDK 17/21 pre-installed).
2. Run the seeder against `/home/ngtukien/Projects/Network-Programming-PTIT`.
3. Automatically test the judge pipeline by submitting at least 2 reference client solutions from the dataset (e.g., one from `./TCP/` and one from `./UDP/` or `./RMI/`) and verifying they achieve `AC`, plus testing a broken client to verify `WA`/`CE` reporting.

## Phase 5: Infrastructure as Code (Terraform on AWS EC2)
Provide a production-ready Terraform configuration in `infra/terraform/` to provision an AWS EC2 instance as the dedicated Judge & App Server:
1. **Terraform Files (`infra/terraform/`):**
   - `main.tf`, `variables.tf`, `outputs.tf`, `terraform.tfvars.example`, and `user_data.sh.tpl`.
   - Use latest Ubuntu LTS AMI (`data.aws_ami`), configurable `instance_type` (default `t3.small` or `t3.medium` for concurrent `javac`/JVM runs), and an EBS root volume (`>= 20 GB` gp3).
2. **Networking & Security Group:**
   - Restrict SSH (`22`) via `var.allowed_ssh_cidr` (defaulting to a safe placeholder, never `0.0.0.0/0` without explicit override).
   - Expose HTTP/HTTPS (`80`, `443`) or the configured frontend/backend ports (`3000`/`8000`) for zero-auth web access, plus an optional configurable TCP/UDP port range if external clients connect directly to Judge Servers.
3. **Automated Bootstrap (`user_data.sh.tpl`):**
   - Install Docker, Docker Compose plugin, Git, and OpenJDK 17/21.
   - Pull/sync the project, run the auto-seeder, and start the full stack with `docker compose up -d --build` (configured to restart automatically).
4. **Outputs & Validation:**
   - Output `instance_id`, `public_ip`, `app_url`, and the SSH command.
   - Verify with `terraform fmt -check` and `terraform validate` (without requiring live AWS credentials during local build).

# Execution Rules
- Begin immediately by inspecting `./Bai-tap-Lap-trinh-mang-Hungdn-2024.pdf` and sampling files in `./TCP`, `./UDP`, `./RMI`, `./WS`, `./SOAP`.
- Keep the existing dataset directories (`TCP/`, `UDP/`, `RMI/`, `WS/`, `SOAP/`, `Bai-tap-Lap-trinh-mang-Hungdn-2024.pdf`) untouched as read-only source data; place the new application code in clean project folders (e.g., `backend/`, `frontend/`, `judge/`, `scripts/`, `infra/terraform/`).
- Keep the codebase modular so adding a new problem later is as simple as adding a folder/spec and re-running the seeder.