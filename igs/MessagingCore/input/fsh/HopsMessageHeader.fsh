Profile: HopsMessageHeader
Parent: MessageHeader
* insert MinDomainResourceRuleSet
* id 1..1
* event[x] only Coding
  * insert MinCodingRuleSet
* destination
  * id 0..0
  * extension 0..0
  * modifierExtension 0..0
  * name 0..0
  * target 0..0
  * endpoint
    * obeys valid-her-id
    * ^example.label = "Fully qualified HER-id"
    * ^example.valueUrl = "urn:oid:2.16.578.1.12.4.1.2.131725"
  * receiver 0..0
* sender 0..0
* enterer 0..0
* author 0..0
* source
  * id 0..0
  * extension 0..0
  * modifierExtension 0..0
  * name 0..0
  * software 0..0
  * version 0..0
  * contact 0..0
* responsible 0..0
* reason 0..0
* response
  * id 0..0
  * extension 0..0
  * modifierExtension 0..0
  * details
    * insert MinReferenceRuleSet
* focus
  * insert MinReferenceRuleSet
* definition 0..0