```mermaid
graph TD
  A[クライアント] --> B[Controller]
  B --> C[Service]
  C --> D[Repository]
  D --> E[DB]
