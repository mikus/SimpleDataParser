# Simple Data Parser

Simple data parser and filter recognizing boolean expression and value (with one-time variable substitution) comparisons.

## Grammar (EBNF)

    <expression> ::= <term> {<or> <term>}
    <term> ::= <factor> {<and> <factor>}
    <factor> ::= <comparison> | (<expression>)
    <comparison> ::= <value> <operator> <value>
    <value> ::= <variable> | <number>
    <operator> ::= > | >= | < | <= | = | !=
    <or> ::= OR | or
    <and> ::= AND | and
    
    
## How to use it

1. Build project

    ```bash
    ./gradlew build
    ```
    
1. Run program with path to data file as an argument

    ```bash
    java -jar build/libs/sdp-1.0.jar data/iris.data
    ```

1. Enter selector to filter rows

        Enter data selector for attributes [sl, sw, pl, pw]:  (sw < 3 OR sw >= 4) AND pw = 0.2 

1. Look at the constructed abstract syntax tree

        AST for entered selector:
        └── AND
            ├── OR
            │   ├── <
            │   │   ├── sw
            │   │   └── 3.0
            │   └── >=
            │       ├── sw
            │       └── 4.0
            └── =
                ├── pw
                └── 0.2

1. Accept selector or deny and enter new one

        Do you accept it? [y/n]:  y

1. Look at the selected rows

        Selected rows:
        {sw=2.9, pw=0.2, sl=4.4, pl=1.4}
        {sw=4.0, pw=0.2, sl=5.8, pl=1.2}
        {sw=4.2, pw=0.2, sl=5.5, pl=1.4}
        
        Result:  3 out of 150 rows selected in 0.028s

## Data files

### File format

Input files should contain [tab-separated values](https://en.wikipedia.org/wiki/Tab-separated_values).
If first line contains values that start with a letter it is treated as a header and its values as attribute names.
Otherwise attribute names are automatically generated as follows: `a`, `b`, ..., `z`, `aa`, `ab`, .... 
All values outside header should be numerical.

### Sample data

Sample data sets located in `data` directory are taken from [UC Irvine Machine Learning Repository](https://archive.ics.uci.edu/ml/datasets.html).


## License

Look inside `LICENSE` file.