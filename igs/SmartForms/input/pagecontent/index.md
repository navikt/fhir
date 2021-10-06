### Scope
This [FHIR Implementation Guide](https://www.hl7.org/fhir/implementationguide.html) (IG) describes a **proposal** to how NAV's SMART on FHIR form-filler application can integrate with an Electronic Health Record (EHR) system and what capabilities that are required by the EHR's FHIR API.

### Actors
The primary actors in context of this IG are:
1.  **EHRs**, "Elektronisk Pasient Journal (EPJ)", are systems that contains and manages health information related to a patient and the tools used by healthcare professionals to interact with this data.
1.  **SMART App** is a SMART on FHIR web application provided by NAV that allows healthcare professionals to report events and to respond to request for information from NAV.
1.  **NAV APIs** is a set of APIs provided by NAV that the SMART app uses to interact with NAV.

### Workflow Overview
Following is a simplified diagram of the steps involved in capturing data for NAV forms. This workflow is based on the [Structure Data Capture FHIR IG](http://hl7.org/fhir/uv/sdc/2019May/index.html).

Dotted-lines are used to represent *optional* sequences.

[![](https://mermaid.ink/img/eyJjb2RlIjoic2VxdWVuY2VEaWFncmFtXG4gICAgcGFydGljaXBhbnQgRSBhcyBFSFJcbiAgICBwYXJ0aWNpcGFudCBBIGFzIFNNQVJUIEFwcFxuICAgIHBhcnRpY2lwYW50IE4gYXMgTkFWXG4gICAgRS0-PitBOiBMYXVuY2ggYXBwXG4gICAgcmVjdCByZ2JhKDI1NSwgMCwgMCwgLjEpXG4gICAgTm90ZSByaWdodCBvZiBBOiBEZWZpbml0aW9uIHJlc291cmNlc1xuICAgIEEtPj4rTjogR0VUIFF1ZXN0aW9ubmFpcmVcbiAgICBOLT4-LUE6IFF1ZXN0aW9ubmFpcmVcbiAgICBBLT4-K046IEdFVCBWYWx1ZVNldFxuICAgIE4tPj4tQTogVmFsdWVTZXRcbiAgICBlbmRcbiAgICByZWN0IHJnYmEoMCwgMjU1LCAwLCAuMSlcbiAgICBOb3RlIHJpZ2h0IG9mIEU6IENvbnRleHQgcmVzb3VyY2VzXG4gICAgQS0tPj4rRTogR0VUIFBhdGllbnRcbiAgICBFLS0-Pi1BOiBQYXRpZW50XG4gICAgQS0tPj4rRTogR0VUIFByYWN0aXRpb25lclxuICAgIEUtLT4-LUE6IFByYWN0aXRpb25lclxuICAgIGVuZFxuICAgIHJlY3QgcmdiYSgwLCAwLCAyNTUsIC4xKVxuICAgIE5vdGUgb3ZlciBBOiBBcnRpZmFjdHNcbiAgICBBLT4-TjogUE9TVCBRdWVzdGlvbm5haXJlUmVzcG9uc2VcbiAgICBBLS0-PkU6IFBPU1QgQmluYXJ5IChQREYpXG4gICAgQS0tPj5FOiBQT1NUIERvY3VtZW50UmVmZXJlbmNlXG4gICAgQS0tPj4tRTogUE9TVCBRdWVzdGlvbm5haXJlUmVzcG9uc2VcbiAgICBlbmRcbiAgICAgICAgICAgICIsIm1lcm1haWQiOnsidGhlbWUiOiJkZWZhdWx0In0sInVwZGF0ZUVkaXRvciI6ZmFsc2V9)](https://mermaid-js.github.io/docs/mermaid-live-editor-beta/#/edit/eyJjb2RlIjoic2VxdWVuY2VEaWFncmFtXG4gICAgcGFydGljaXBhbnQgRSBhcyBFSFJcbiAgICBwYXJ0aWNpcGFudCBBIGFzIFNNQVJUIEFwcFxuICAgIHBhcnRpY2lwYW50IE4gYXMgTkFWXG4gICAgRS0-PitBOiBMYXVuY2ggYXBwXG4gICAgcmVjdCByZ2JhKDI1NSwgMCwgMCwgLjEpXG4gICAgTm90ZSByaWdodCBvZiBBOiBEZWZpbml0aW9uIHJlc291cmNlc1xuICAgIEEtPj4rTjogR0VUIFF1ZXN0aW9ubmFpcmVcbiAgICBOLT4-LUE6IFF1ZXN0aW9ubmFpcmVcbiAgICBBLT4-K046IEdFVCBWYWx1ZVNldFxuICAgIE4tPj4tQTogVmFsdWVTZXRcbiAgICBlbmRcbiAgICByZWN0IHJnYmEoMCwgMjU1LCAwLCAuMSlcbiAgICBOb3RlIHJpZ2h0IG9mIEU6IENvbnRleHQgcmVzb3VyY2VzXG4gICAgQS0tPj4rRTogR0VUIFBhdGllbnRcbiAgICBFLS0-Pi1BOiBQYXRpZW50XG4gICAgQS0tPj4rRTogR0VUIFByYWN0aXRpb25lclxuICAgIEUtLT4-LUE6IFByYWN0aXRpb25lclxuICAgIGVuZFxuICAgIHJlY3QgcmdiYSgwLCAwLCAyNTUsIC4xKVxuICAgIE5vdGUgb3ZlciBBOiBBcnRpZmFjdHNcbiAgICBBLT4-TjogUE9TVCBRdWVzdGlvbm5haXJlUmVzcG9uc2VcbiAgICBBLS0-PkU6IFBPU1QgQmluYXJ5IChQREYpXG4gICAgQS0tPj5FOiBQT1NUIERvY3VtZW50UmVmZXJlbmNlXG4gICAgQS0tPj4tRTogUE9TVCBRdWVzdGlvbm5haXJlUmVzcG9uc2VcbiAgICBlbmRcbiAgICAgICAgICAgICIsIm1lcm1haWQiOnsidGhlbWUiOiJkZWZhdWx0In0sInVwZGF0ZUVkaXRvciI6ZmFsc2V9)

In order to discover and launch the SMART app the EHR must support the [SMART Application Launch Framework](https://hl7.org/fhir/smart-app-launch/).

Once launched the SMART app must have be able to connect to the NAV back-end services to fetch the required definition resources; Questionnaire, ValueSets, CodeSystem etc. These resources are not static and will be continuously developed and improved by NAV teams in response to feedback from healthcare and social work professionals.

The EHR can *optionally* implement capabilities to access context relevant data as FHIR resources. This data can be used by the SMART app to auto-fill forms, thereby lessen the work required by the user.

The form answers will be sent to NAV as FHIR QuestionnaireResponse resources with a status (in-progress, competed etc.).

Depending on the requirements of the EHR, notably archiving, they can *optionally* implement capabilities that allows the SMART app to send completed or intermediate representations of the forms back to the EHR. This can either be as a raw QuestionnaireResponse, or as a human-readable PDF with associated metadata (Binary + DocumentReference).