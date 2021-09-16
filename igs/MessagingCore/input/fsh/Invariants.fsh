Invariant: valid-uuid
Description: "Must be a valid uuid"
Expression: "$this.matches('^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$')"
Severity: #error

// HER-ID is an integer [0, 10000000], ref. https://register-web.test.nhn.no/docs/arkitektur/register/ar.html#begrensninger
Invariant: valid-her-id
Description: "Must be a valid HER-ID prefixed with OID namespace"
Expression: "$this.matches('^urn:oid:2\\\\.16\\\\.578\\\\.1\\\\.12\\\\.4\\\\.1\\\\.2\\\\.[1-9][0-9]{0,7}$')"
Severity: #error
