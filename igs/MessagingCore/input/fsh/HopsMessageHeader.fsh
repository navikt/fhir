Profile: HopsMessageHeader
Parent: MessageHeader
* insert MinDomainResourceRuleSet
* event[x] only Coding
  * insert MinCodingRuleSet
* destination
  * insert MinBackboneElementRuleSet
  * name 0..0
  * target 0..0
  * receiver 0..0
* sender 0..0
* enterer 0..0
* author 0..0
* source
  * insert MinBackboneElementRuleSet
  * name 0..0
  * software 0..0
  * version 0..0
  * contact 0..0
* responsible 0..0
* reason 0..0
* response
  * insert MinBackboneElementRuleSet
  * details
    * insert MinReferenceRuleSet
* focus
  * insert MinReferenceRuleSet
* definition 0..0