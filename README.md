# Slickless GetResult for Case Classes

```
[info] caseclass-getresult.scala:62: Example.caseClassGetResult is not a valid implicit value for slick.jdbc.GetResult[Example.SimpleInt] because:
[info] hasMatchingSymbol reported error: ambiguous implicit values:
[info]  both value genSimpleString in object Example of type => shapeless.Generic[Example.SimpleString]{type Repr = shapeless.::[String,shapeless.HNil]}
[info]  and value genSimpleInt in object Example of type => shapeless.Generic[Example.SimpleInt]{type Repr = shapeless.::[Int,shapeless.HNil]}
[info]  match expected type shapeless.Generic.Aux[C,H]
[info]   implicitly[GetResult[SimpleInt]]
[info]             ^
[error] caseclass-getresult.scala:62: could not find implicit value for parameter e: slick.jdbc.GetResult[Example.SimpleInt]
[error]   implicitly[GetResult[SimpleInt]]
[error]             ^
[error] one error found
[error] (compile:compile) Compilation failed
```