RuleSet: MinElementRuleSet
* id 0..0
* extension 0..0

RuleSet: MinBackboneElementRuleSet
* insert MinElementRuleSet
* modifierExtension 0..0

RuleSet: MinDomainResourceRuleSet
* meta
  * insert MinElementRuleSet
  * profile 0..0
  * security 0..0
  * tag 0..0
* implicitRules 0..0
* language 0..0
* text
  * insert MinElementRuleSet
* contained 0..0
* extension 0..0
* modifierExtension 0..0