Profile: HopsMessage
Parent: Bundle
* id obeys valid-uuid
* meta
  * id 0..0
  * profile 1..1
  * security 0..0
  * tag 0..0
* implicitRules 0..0
* language 0..0
* identifier 0..0
* type = #message (exactly)
* timestamp 1..1
* total 0..0
* link 0..0
* entry 2..
  * ^slicing.discriminator.type = #type
  * ^slicing.discriminator.path = "resource"
  * ^slicing.rules = #open
  * id 0..0
  * extension 0..0
  * modifierExtension 0..0
  * link 0..0
  * fullUrl 1..1
  * resource 1..1
  * search 0..0
  * request 0..0
  * response 0..0
* entry contains messageheader 1..1
* entry[messageheader].resource only HopsMessageHeader
* signature 0..0