ZBASIC
======

ZBASIC - the simple basic compiler, written on Java.

Build and run
-------------

First, zbasic-core-*<version>*.jar library must be installed in local Maven repository.

For build zbasic-core library: **mvn** **package**

For build and install zbasic-core library: **mvn** **install**

For build zbasic-cli, zbasic-gui, zbasic-web: **mvn** **package**

Applications:
**zbasic-core** - zbasic core library (compiler for everyone)
**zbasic-cli**  - console application
**zbasic-gui**  - GUI application (editor)
**zbasic-web**  - Web application (REST service)

Compile from command line:
**java** **-jar** **zbasic-cli.jar** [*""<platform-name>""* ] *""<source-file>""* *""<target-file>""*

Comments
--------

Single line comment (skip by compiler):

**REM** *""<comment>""*

Data types and operators
------------------------

Two data types used: integer numbers and strings, also supported number arrays.

Constants:
**FALSE** = 0 - false value in conditions
**TRUE** = -1 - true value in conditions (or any non-zero number)

Operators:
Call functions: *""<function>""*(*""<arg>""**{*, *""<arg>""**}*)
Call function with 1 argument: *""<function>""* *""<arg>""*
Arithmetic operators: - + * / (integer division) % (mod)
String operators: & (concat)
Comparation operators: = &lt;&gt;  &gt; &lt; &gt;= &lt;=
Logical operators: **NOT** **AND** **OR**

Modules
-------

Module is indepenedent compiled unit, with data and code in one file.

Structure of module:
*{***IMPORT** *[**package**{*.*subpackage**}*.*]*ModuleName*[*Name*{*, Name*}**]**}*
*{* *[***EXPORT***]* *""<constant-variable-array-function-or-subroutine-declaration>""**}*
*[***BEGIN**
  *{**""<statement>""**}*
**END***]*

Module can import names from another modules with EXPORT modifier.

Declare constants:
**CONST** ConstantName*[*$*]* = *""<constant-value>""*

Declare variables with initialization:
**VAR** VarName*[*$*]* = *""<variable-value>""*

Declare variables without initialization:
**VAR** VarName*[*$*]**{*, VarName*[*$*]**}*

Declare static arrays:
**DIM** ArrayName[*""<size>""*] *[***DATA** *""<value>""**{*, *""<value>""**}**]*

Declare dynamic arrays:
**DIM** ArrayName[]

Declare simple functions:
**DEF** FunctionName*[*$*]*](*[*argName*[*$*]**{*, argName*[*$*]**}**]*) = *""<expression>""*

Declare multiline functions:
**FUNCTION** FunctionName*[*$*]*(*[*argName*[*$*]**{*, argName*[*$*]**}**]*)
*{***VAR** localVarName*[*$*]**{*, localVarName*[*$*]**}**}*
*{**""<statement>""**}*
**END** **FUNCTION**

Declare subroutines:
**SUB** SubroutineName(*[*argName*]*)
*{***VAR** localVarName*[*$*]**{*, localVarName*[*$*]**}**}*
*{**""<statement>""**}*
**END** **SUB**

Functions
---------

**ABS**(*number*) - absolute value of number
**BAND**(*n*, *m*) - binary AND operator (get share bites)
**BIT**(*number*, *""<bit>""*) - check bit (0-15) in number
**BNOT**(*n*) - binary NOT operator (inverse bites)
**BOR**(*n*, *m*) - binary OR operator (unite bites)
**BOUND**(*""<array>""*) - size of array
**BXOR**(*n*, *m*) - binary XOR operator (get difference of bites)
**CHR$**(*charCode*) - character by ASCII code
**CODE**(*c$*) - ASCII code of character
**INSTR**(*[**startIndex**]*, *string$*, *subString$*) - find substring (0 if not found or index)
**LEFT$**(*string$*, *count*) - left substring
**LEN**(*string$*) - length of string
**LSH**(*number*, *""<bit-count>""*) - left shift for number
**MID$**(*string$*, *index*, *count*) - middle substring
**RANDOM**(*limit*) - get integer random number in range from 0 to (limit - 1)
**RIGHT$**(*string$*, *count*) - right substring
**RSH**(*number*, *""<bit-count>""*) - signed right shift for number
**STR$**(*number*) - convert number to string
**USH**(*number*, *""<bit-count>""*) - unsigned right shift for number
**VAL**(*string$*) - convert string to number (0 if invalid string)

Simple statements
-----------------

**BRESET** *""<lvalue>""*, *""<bit>""* - reset bit in number variable
**BSET** *""<lvalue>""*, *""<bit>""* - set bit in number variable
**[CALL]** *""<subroutine>""**{*, *""<arg>""**}* - call subroutine
**CLS** - clear screen
**COPY** *""<src-array>""*, *srcIndex*, *""<dest-array>""*, *destIndex*, *count* - copy arrays
**DEC** *""<lvalue>""**{*, *step**}* - decrease number variable
**FILL** *""<array>""*, *startIndex*, *count*, *value* - fill array
**FREE** *""<array>""* - remove dynamic array from memory
**INC** *""<lvalue>""**{*, *step**}* - increase number variable
**INPUT** *""<lvalue>""* - input value of number or string variable from console
**[LET]** *""<lvalue>""* = *""<new-value>""* - set new value to variable
**PAUSE** *time* - pause program on time (in ticks, 10 per second)
**QUIT** - terminate program
**READ** *""<lvalue>""**{*, *""<lvalue>""**}* - get new value from data source and move pointer forward
**REDIM** *""<array>""**[**""<new-size>""**]* - set new size to dynamic array
**RESTORE** *""<data-source>""* - set data source for READ statement (string or array)
**RETURN** *[**""<result>""**]* - return from function or subroutine
**SWAP** *""<first-lvalue>""*, *""<second-lvalue>""* - swap values of two varaibles

Conditional statements
----------------------

Simple IF statement:
**IF** *""<condition>""* **THEN** *""<statement>""*

Multiwise IF statement:
**IF** *""<condition>""* **THEN**
  *{**""<statement>""**}*
*{***ELSE** **IF** *""<condition>""* **THEN**
  *{**""<statement>""**}* *}*
*[***ELSE**
  *{**""<statement>""**}* *]*
**END** **IF**

Multiwise SELECT statement:
**SELECT** **CASE** *""<case-value>""*
*{***CASE** *""<variant>""**{*, *""<variant>""**}*
  *{**""<statement>""**}* *}*
*[***DEFAULT**
  *{**""<statement>""**}* *]*
**END** **SELECT**

Cycle statements
----------------

Sequetional FOR cycle:
**FOR** *""<counter>""* = *""<start>""* **TO** *""<limit>""* *[***STEP** *""<s>""**]*
  *{**""<statement>""**}*
**END** **FOR**

Conditional WHILE cycle (repeated if true condition):
**WHILE** *""<condition>""*
  *{**""<statement>""**}*
**END** **WHILE**

Conditional REPEAT cycle (stopped when true condition):
**REPEAT**
  *{**""<statement>""**}*
**UNTIL** *""<exit-condition>""*

The WITH statement
------------------

Enter to namespace of module:
**WITH** *""<namespace>""*
  *{**""<statement>""**}*
**END** **WITH**

