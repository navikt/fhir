This [FHIR Implementation Guide](https://www.hl7.org/fhir/implementationguide.html) (IG) describes a **proposal** to how the [FHIR messaging pattern](https://www.hl7.org/fhir/messaging.html) can be used in communication between NAV and Electronic Health Record (EHR) systems.

There are currently no national IG on how FHIR messaging shall be used in Norway, but the need for such an IG [has been recognized](https://github.com/HL7Norway/best-practice/blob/master/docs/messaging.md) by The Norwegian Directorate of eHealth, partially to replace the currently used ebXML-based [Dialogmelding](https://www.ehelse.no/Standardisering/standarder/dialogmelding-v1.1). This is why NAV is developing this IG, which hopefully can contribute in creating an official national IG accepted by the Norwegian health sector. NAV is committed to adhere to whatever standards or best-practices the Norwegian health sector adopts, and have no interest in creating a separate custom integration pattern, it is therefore important to recognize this IG as a *proposal*, which is open to feedback and adjustments.

### Actors
The primary actors in context of this IG are:
1.  **EHRs**, "Elektronisk Pasient Journal (EPJ)", are systems that contains and manages health information related to a patient and the services that operate on this data.
1.  **NAV** is the Norwegian Labour and Welfare Administration and includes all the services managed by NAV.

### Messaging vs. RESTful
FHIR Messaging and FHIR RESTful API are two patterns defined in the [FHIR specification](https://www.hl7.org/fhir/exchange-module.html).

The RESTful API pattern is the most stable and widely used pattern for exchanging FHIR resources. This pattern is well suited for exchanging data between front-end apps and back-end services, e.g. SMART-on-FHIR apps.

The Messaging pattern can be more suitable for exchange between disparate organizations with low levels of process integration and/or trust. For instance, it might be preferred by an organization/system that has not necessarily adopted FHIR internally to receive a FHIR message containing a coherent snapshot of the complete context. The alternative would be to pull the required resources from multiple REST endpoints, where the lifecycle and versioning of these resources also must be considered.

Figure 1 visualizes exchange of FHIR messages between three different organizations, each having multiple internal systems that communicates using different patterns and protocols.

#### Figure 1
[![](https://mermaid.ink/img/eyJjb2RlIjoiZmxvd2NoYXJ0ICBcbiAgICBzdWJncmFwaCBOQVZcbiAgICBkaXJlY3Rpb24gTFJcbiAgICB5MS0tPnxGSElSIFJFU1RmdWx8eTJcbiAgICB5MS0tPnxLYWZrYXx5M1xuICAgIGVuZFxuICAgIHN1YmdyYXBoIEVIUjJcbiAgICB6Mi0tPnoxXG4gICAgejEtLT58cHJvcHJpZXRhcnl8ejJcbiAgICB6My0tPnxvcGVuRUhSfHoyXG4gICAgZW5kXG4gICAgc3ViZ3JhcGggRUhSMVxuICAgIGRpcmVjdGlvbiBMUlxuICAgIHgxLS0-eDJcbiAgICB4Mi0tPnxGSElSIFJFU1RmdWx8eDNcbiAgICB4My0tPngyXG4gICAgeDQtLT58cHJvcHJpZXRhcnl8eDNcbiAgICBlbmRcbiAgICBFSFIxLS4gRkhJUiBNZXNzYWdpbmcgLi0-TkFWXG4gICAgTkFWLS4tPkVIUjFcbiAgICBOQVYtLiBGSElSIE1lc3NhZ2luZyAuLT5FSFIyXG4gICAgRUhSMi0uLT5OQVZcbiAgICBzdHlsZSBFSFIxIGZpbGw6IzU0OTlDNyxzdHJva2U6IzMzM1xuICAgIHN0eWxlIE5BViBmaWxsOiNFNzRDM0Msc3Ryb2tlOiMzMzMiLCJtZXJtYWlkIjp7InRoZW1lIjoiZGVmYXVsdCJ9LCJ1cGRhdGVFZGl0b3IiOmZhbHNlLCJhdXRvU3luYyI6dHJ1ZSwidXBkYXRlRGlhZ3JhbSI6ZmFsc2V9)](https://mermaid-js.github.io/mermaid-live-editor/edit/#eyJjb2RlIjoiZmxvd2NoYXJ0ICBcbiAgICBzdWJncmFwaCBOQVZcbiAgICBkaXJlY3Rpb24gTFJcbiAgICB5MS0tPnxGSElSIFJFU1RmdWx8eTJcbiAgICB5MS0tPnxLYWZrYXx5M1xuICAgIGVuZFxuICAgIHN1YmdyYXBoIEVIUjJcbiAgICB6Mi0tPnoxXG4gICAgejEtLT58cHJvcHJpZXRhcnl8ejJcbiAgICB6My0tPnxvcGVuRUhSfHoyXG4gICAgZW5kXG4gICAgc3ViZ3JhcGggRUhSMVxuICAgIGRpcmVjdGlvbiBMUlxuICAgIHgxLS0-eDJcbiAgICB4Mi0tPnxGSElSIFJFU1RmdWx8eDNcbiAgICB4My0tPngyXG4gICAgeDQtLT58cHJvcHJpZXRhcnl8eDNcbiAgICBlbmRcbiAgICBFSFIxLS4gRkhJUiBNZXNzYWdpbmcgLi0-TkFWXG4gICAgTkFWLS4tPkVIUjFcbiAgICBOQVYtLiBGSElSIE1lc3NhZ2luZyAuLT5FSFIyXG4gICAgRUhSMi0uLT5OQVZcbiAgICBzdHlsZSBFSFIxIGZpbGw6IzU0OTlDNyxzdHJva2U6IzMzM1xuICAgIHN0eWxlIE5BViBmaWxsOiNFNzRDM0Msc3Ryb2tlOiMzMzMiLCJtZXJtYWlkIjoie1xuICBcInRoZW1lXCI6IFwiZGVmYXVsdFwiXG59IiwidXBkYXRlRWRpdG9yIjpmYWxzZSwiYXV0b1N5bmMiOnRydWUsInVwZGF0ZURpYWdyYW0iOmZhbHNlfQ)

### Workflow
The FHIR Messaging pattern does not describe *how* messages should be transferred. This can be done using Kafka, MQ, FTP, file-store, email, HTTP (including FHIR RESTful API) or any other current or future protocol. Messaging is also not limited to using a single protocol: the same message can be available on multiple channels or be passed through multiple protocols before arriving at the destination.

A simple and powerful way to exchange messages is to simply provide two endpoints: Publish and Retrieve. The [specification](https://www.hl7.org/fhir/messaging.html#process) contains examples of this using the FHIR RESTful API; Publish can be implemented as either `POST: {base}/$process-message` or `POST: {base}/Bundle`, and retrieval can be implemented as `GET: {base}/Bundle`. There is a choice between using $process-message and Bundle for publish; NAV is leaning towards the former due to it already being used by [Da-Vinci project](http://build.fhir.org/ig/HL7/davinci-alerts/branches/master/guidance.html) and [NHS Digital](https://simplifier.net/guide/DigitalMedicines/process-message).

HTTP and REST as a message\event exchange protocol might be perceived by some as "old fashion", but it is a well established protocol that all developers should be familiar with and serves well as a common denominator. Integrators are still able to forward the messages to whatever internal event-streaming technology they prefer.

#### Figure 2
[![](https://mermaid.ink/img/eyJjb2RlIjoic2VxdWVuY2VEaWFncmFtXG4gICAgcmVjdCByZ2JhKDAsIDI1NSwgMCwgLjEpXG4gICAgbG9vcCBwZXJpb2RpYyBwb2xsaW5nXG4gICAgRUhSLT4-TkFWOiBHRVQgL0J1bmRsZT9tZXNzYWdlLmRlc3RpbmF0aW9uLXVyaT17aGVyLWlkfSZfb2Zmc2V0PXtvZmZzZXR9XG4gICAgTkFWLT4-RUhSOiBTZWFyY2hSZXN1bHRzW01lc3NhZ2VdXG4gICAgZW5kXG4gICAgZW5kXG5cbiAgICByZWN0IHJnYmEoMjU1LCAwLCAwLCAuMSlcbiAgICBFSFItPj5OQVY6IFBPU1QgLyRwcm9jZXNzLW1lc3NhZ2VcbiAgICBOQVYtPj5FSFI6IDIwMiBBY2NlcHRlZFxuICAgIGVuZCIsIm1lcm1haWQiOnsidGhlbWUiOiJkZWZhdWx0In0sInVwZGF0ZUVkaXRvciI6ZmFsc2UsImF1dG9TeW5jIjp0cnVlLCJ1cGRhdGVEaWFncmFtIjpmYWxzZX0)](https://mermaid-js.github.io/mermaid-live-editor/edit/#eyJjb2RlIjoic2VxdWVuY2VEaWFncmFtXG4gICAgcmVjdCByZ2JhKDAsIDI1NSwgMCwgLjEpXG4gICAgbG9vcCBwZXJpb2RpYyBwb2xsaW5nXG4gICAgRUhSLT4-TkFWOiBHRVQgL0J1bmRsZT9tZXNzYWdlLmRlc3RpbmF0aW9uLXVyaT17aGVyLWlkfSZfb2Zmc2V0PXtvZmZzZXR9XG4gICAgTkFWLT4-RUhSOiBTZWFyY2hSZXN1bHRzW01lc3NhZ2VdXG4gICAgZW5kXG4gICAgZW5kXG5cbiAgICByZWN0IHJnYmEoMjU1LCAwLCAwLCAuMSlcbiAgICBFSFItPj5OQVY6IFBPU1QgLyRwcm9jZXNzLW1lc3NhZ2VcbiAgICBOQVYtPj5FSFI6IDIwMiBBY2NlcHRlZFxuICAgIGVuZCIsIm1lcm1haWQiOiJ7XG4gIFwidGhlbWVcIjogXCJkZWZhdWx0XCJcbn0iLCJ1cGRhdGVFZGl0b3IiOmZhbHNlLCJhdXRvU3luYyI6dHJ1ZSwidXBkYXRlRGlhZ3JhbSI6ZmFsc2V9)

### Polling vs. Pushing
Polling is often simpler to implement and test than a Push-based exchange, especially when reliability, throttling and retries are taken into consideration.

NAV foresee a FHIR RESTful compliant service with the ability to query messages using [Bundle search parameters](https://www.hl7.org/fhir/bundle.html#search). These messages are represented as a long list of immutable messages ordered from first to last that can be paginated using an offset parameter. Clients can then consume messages at their own pace and even "replay" old messages, new messages are awaited by continuously poll with an offset equal the last entry, this pattern is similar to interacting with a Kafka partition.

### Addressing (HER-id)
FHIR Messaging requires a way to identify a message's *source* and *destination* endpoints. These endpoints can be either a protocol-specific endpoint, such as a HTTP URL or email-address, or an identifier used to identify a logical entity without specifying *how* the message should be delivered.

In order to be technology agnostic, the message-model must be separated from the transfer-protocol. This allows a message to be routed unmodified through multiple intermediates and protocols before arriving at the destination.

In Norway we already have an established standard to identify communicating entities within the health sector called [Tjenestebasert adressering](https://www.ehelse.no/standardisering/om-standardisering-i-e-helse/tjenestebasert-adressering#Om%20tjenestebasert%20adressering%20og%20tjenestetyper). This standard does not force any particular transfer-protocol and can therefore be used to identify *source* and *destination* as logical entities in a FHIR message. Such an identifier is called *HER-id* and shall be populated in the required message elements: `source.endpoint` and `destination.endpoint`. Because these elements requires an URI we use HER-id's [reserved OID namespace](https://www.ehelse.no/teknisk-dokumentasjon/oid-identifikatorserier-i-helse-og-omsorgstjenesten) to create a fully-qualified URI, example:

```json
"destination": [
  {
    "endpoint": "urn:oid:2.16.578.1.12.4.1.2.102287"
  }
]
```