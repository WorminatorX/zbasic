ZBASIC
======

ZBASIC - the simple basic compiler, written on Java.

Build and run
-------------

First, zbasic-core-*&lt;version&gt;*.jar library must be installed in local Maven repository.

For build zbasic-core library: **mvn** **package**

For build and install zbasic-core library: **mvn** **install**

For build zbasic-cli, zbasic-gui, zbasic-web: **mvn** **package**

### Applications:

* **zbasic-core** - zbasic core library (compiler for everyone)
* **zbasic-cli**  - console application
* **zbasic-gui**  - GUI application (editor)
* **zbasic-web**  - Web application (REST service)

### Compile from command line:

**java** **-jar** **zbasic-cli.jar** *[* *&lt;platform-name&gt;* *]* *&lt;source-file&gt;* *&lt;target-file&gt;*

Data types and operators
------------------------

Two data types used: integer numbers and strings, also supported number arrays.

### Constants:

* **FALSE** = 0 - false value in conditions
* **TRUE** = -1 - true value in conditions (or any non-zero number)

### System variables

* **TIMER** - system timer counter (increases every 1/10 sec)

### Operators:

* Get name from namespace (module): *&lt;namespace&gt;*.name
* Get element of array or string: *&lt;array-or-string&gt;*[ *&lt;index&gt;* ]
* Call function: *&lt;function&gt;*(*&lt;arg&gt;* *{*, *&lt;arg&gt;* *}*)
* Call function with 1 argument: *&lt;function&gt;* *&lt;arg&gt;*
* Arithmetic operators: - + * / (integer division) % (mod)
* String operators: & (concat)
* Comparation operators: = &lt;&gt;  &gt; &lt; &gt;= &lt;=
* Logical operators: **NOT** **AND** **OR**

Modules
-------

Module is indepenedent compiled unit, with data and code in one file.

Modules can import names from another modules with EXPORT modifier.

### Structure of module:

*{* **IMPORT** *[* package *{* .subpackage *}* .*]* ModuleName *[* (Name *{* ,Name *}* ) *]* *}*

*{* *[* **EXPORT** *]* *&lt;constant-variable-array-function-or-subroutine-declaration&gt;* *}*

*[* **BEGIN**

*{* *&lt;statement&gt;* *}*

**END** *]*

### Declare constants:

**CONST** ConstantName *[*$*]* = *&lt;constant-value&gt;*

### Declare variables with initialization:

**VAR** VarName *[*$*]* = *&lt;variable-value&gt;*

### Declare variables without initialization:

**VAR** VarName *[*$*]* *{*, VarName *[*$*]* *}*

### Declare static arrays:

**DIM** ArrayName[*&lt;size&gt;*] *[* **DATA** *&lt;value&gt;* *{*, *&lt;value&gt;* *}* *]*

### Declare dynamic arrays:

**DIM** ArrayName[]

### Declare simple functions:

**DEF** FunctionName *[*$*]* (*[*argName *[*$*]* *{*, argName *[*$*]* *}* *]*) = *&lt;expression&gt;*

### Declare multiline functions:

**FUNCTION** FunctionName *[*$*]* (*[*argName *[*$*]* *{*, argName *[*$*]* *}* *]*)

*{* **VAR** localVarName *[*$*]* *{*, localVarName *[*$*]* *}* *}*

*{* *&lt;statement&gt;* *}*

**END** **FUNCTION**

### Declare subroutines:

**SUB** SubroutineName(*[* *argName *[*$*]* *{*, argName *[*$*]* *}* *]*)

*{* **VAR** localVarName *[*$*]* *{*, localVarName *[*$*]* *}* *}*

*{* *&lt;statement&gt;* *}*

**END** **SUB**

Comments
--------

### Single line comments (skip by compiler):

**REM** *&lt;comment&gt;*

Functions
---------

**ABS** ( *number* ) - absolute value of number

**BAND** ( *n*, *m* ) - binary AND operator (get share bites)

**BIT** ( *number*, *&lt;bit&gt;* ) - check bit (0-15) in number

**BNOT** ( *n* ) - binary NOT operator (inverse bites)

**BOR** ( *n*, *m* ) - binary OR operator (unite bites)

**BOUND** ( *&lt;array&gt;* ) - size of array

**BXOR** ( *n*, *m* ) - binary XOR operator (get difference of bites)

**CHR$** ( *charCode* ) - character by ASCII code

**CODE** ( *c$* ) - ASCII code of character

**INSTR** ( *[* *startIndex* *]*, *string$*, *subString$* ) - find substring (0 if not found or index)

**LEFT$** ( *string$*, *count* ) - left substring

**LEN** ( *string$* ) - length of string

**LSH** ( *number*, *&lt;bit-count&gt;* ) - left shift for number

**MID$** ( *string$*, *index* , *count* ) - middle substring

**RANDOM** ( *limit* ) - get integer random number in range from 0 to (limit - 1)

**RIGHT$** ( *string$*, *count* ) - right substring

**RSH** ( *number*, *&lt;bit-count&gt;* ) - signed right shift for number

**STR$** ( *number* ) - convert number to string

**USH** ( *number*, *&lt;bit-count&gt;* ) - unsigned right shift for number

**VAL** ( *string$* ) - convert string to number (0 if invalid string)

Simple statements
-----------------

**BRESET** *&lt;lvalue&gt;*, *&lt;bit&gt;* - reset bit in number variable

**BSET** *&lt;lvalue&gt;*, *&lt;bit&gt;* - set bit in number variable

*[* **CALL** *]* *&lt;subroutine&gt;* *{*, *&lt;arg&gt;* *}* - call subroutine

**CLS** - clear screen

**COPY** *&lt;src-array&gt;*, *srcIndex* , *&lt;dest-array&gt;*, *destIndex* , *count* - copy arrays

**DEC** *&lt;lvalue&gt;* *{* , *step* *}* - decrease number variable

**FILL** *&lt;array&gt;*, *startIndex* , *count* , *value* - fill array

**FREE** *&lt;array&gt;* - remove dynamic array from memory

**INC** *&lt;lvalue&gt;* *{*, *step* *}* - increase number variable

**INPUT** *&lt;lvalue&gt;* - input value of number or string variable from console

*[* **LET** *]* *&lt;lvalue&gt;* = *&lt;new-value&gt;* - set new value to variable

**PAUSE** *time* - pause program on time (in ticks, 10 per second)

**QUIT** - terminate program

**READ** *&lt;lvalue&gt;* *{*, *&lt;lvalue&gt;* *}* - get new value from data source and move pointer forward

**REDIM** *&lt;array&gt;*[*&lt;new-size&gt;*] - set new size to dynamic array

**RESTORE** *&lt;data-source&gt;* - set data source for READ statement (string or array)

**RETURN** *[* *&lt;result&gt;* *]* - return from function or subroutine

**SWAP** *&lt;first-lvalue&gt;*, *&lt;second-lvalue&gt;* - swap values of two varaibles

Conditional statements
----------------------

### Simple IF statement:

**IF** *&lt;condition&gt;* **THEN** *&lt;statement&gt;*

### Multiwise IF statement:

**IF** *&lt;condition&gt;* **THEN**

&nbsp;&nbsp;&nbsp;&nbsp; *{* *&lt;statement&gt;* *}*

*{* **ELSE** **IF** *&lt;condition&gt;* **THEN**

&nbsp;&nbsp;&nbsp;&nbsp; *{* *&lt;statement&gt;* *}* *}*

*[* **ELSE**

&nbsp;&nbsp;&nbsp;&nbsp; *{* *&lt;statement&gt;* *}* *]*

**END** **IF**

### Multiwise SELECT statement:

**SELECT** **CASE** *&lt;case-value&gt;*

*{* **CASE** *&lt;variant&gt;* *{*, *&lt;variant&gt;* *}*

&nbsp;&nbsp;&nbsp;&nbsp; *{* *&lt;statement&gt;* *}* *}*

*[* **DEFAULT**

&nbsp;&nbsp;&nbsp;&nbsp; *{* *&lt;statement&gt;* *}* *]*

**END** **SELECT**

Cycle statements
----------------

### Sequetional FOR cycle:

**FOR** *&lt;counter&gt;* = *&lt;start&gt;* **TO** *&lt;limit&gt;* *[* **STEP** *&lt;s&gt;* *]*

&nbsp;&nbsp;&nbsp;&nbsp; *{* *&lt;statement&gt;* *}*

**END** **FOR**

### Conditional WHILE cycle (repeated if true condition):

**WHILE** *&lt;condition&gt;*

&nbsp;&nbsp;&nbsp;&nbsp; *{* *&lt;statement&gt;* *}*

**END** **WHILE**

### Conditional REPEAT cycle (stopped when true condition):

**REPEAT**

&nbsp;&nbsp;&nbsp;&nbsp; *{* *&lt;statement&gt;* *}*

**UNTIL** *&lt;exit-condition&gt;*

The WITH statement
------------------

### Enter inside to namespace of module:

**WITH** *&lt;namespace&gt;*

&nbsp;&nbsp;&nbsp;&nbsp; *{* *&lt;statement&gt;* *}*

**END** **WITH**

