# Programing

Read this if you know absolutly nothing about programing. In fact, read even if you do know something just to make sure you understand everything you need to.

*Note* The code here is pseudo code, so it dosent follow any spicific examples, it's just used to explane concepts

## The Basics

When you write code, you save it to a text (ie: all the charecters are ascii or unicode encoded) file wich is read from top to bottom much like a book. This is great for you because it's easy to read and understand. But for the computer, it means nothing. So your code gets converted to **machine code** (1 and 0 which can be fed directly to the CPU) by a **compiler**.
```
--------                            ----------------
| Your |         |----------|       |              |        -----
| Code |   -->   | Compiler |  -->  | Machine Code |  -->   |CPU|
| File |         |----------|       |              |        -----
--------                            ----------------
```
Now a computer at its most basic level is just two things, a **CPU** and **RAM** 
* Data is stored in RAM
* Operations are exicuted in the CPU

You can get very indepth with this, but for now you only need a basic idea

### Binary

You often hear that computers think in 1 and 0, this is because at the most basic level, everything is represented in 1 and 0.

Whether you are aware or not, we use a base 10 counting system. This means we have 10 unique digits (0123456789). When you have more then 9, you put a 1 in the tens place to say you have 1 group of 10. 23 is the same as saying 2 groups of ten and 3 groups of 1. 
(2 x 10) + (3 x 1) = 23

This is the same in binary, but insead of having 10 digits, you have 2 (**bi**-nary). In the first "column" you can represent two numbers, 0 and 1. After that, you move over a "column" and put a 1, to represent the you have 1 group of 2 (the group size is 2, bucasue the total number of numbers you could represent is 2 (0 and 1)). To represent 3, you would write 11, or 1 group of 2 and 1 group of 1.
(1x2) + (1x1) = 3
After 3 we have to move over to the next column to represent 4. 100 in binary is 4 in base-10 because with two columns you can represent  4 numbes (0, 1, 2, and 3). Five is 101 ((1x4) + (0x2) + (1x1)) and six is 110.

* 0 : 0000
* 1 : 0001
* 2 : 0010
* 3 : 0011
* 4 : 0100
* 5 : 0101
* 6 : 0110
* 7 : 0111
* 8 : 1000
* 9 : 1001
* 10 : 1010
* 11 : 1011
* 12 : 1100
* 13 : 1101
* 14 : 1110
* 15 : 1111

Two go from binar to base-10 is easy

Add up every column like follows:
(number in column) x 2^(column place)

Binary was chosen for computers for two reasons.
1. It was easly represented with electricry, either there was a current or there wasn't
2. There was already a whole system in place to deal with it called **boolean logic**.

### Varibles

Varibles are a very importent part of programing, you can think of them like a container or a way to lable data. 
```
myVar = 5
```
They are importent because there are many things you don't know ahead of time but still would like to access. For example, lets say you are writting a program where the user is trying to guess a number. You don't know what number they are going to guess, so you assine a varible to it. You can also assine varibles beforehand that will be used later. 

Let's say you want to add two numbers 4 and 5. You would declare a varible holding a 5, one holding a 4, and one to keep the answer.

```
myVar = 5
myOtherVar = 4
theAnswer = myVar + myOtherVar
```

Right now *theAnswer* is "holding" the number 9. You will notice that even though I used the variable names, the computer understood that I was doing math and added the numbers the varibles were holding.

You can perform basic aritmitc on varibles (and much more)

* +: add two numbers
* -: subtract two numbers
* \*: multiply two numbers
* /: divide two numbers
* (): do the stuff in the parentheses first

### Data Types

Sometimes you want to stor something that's not a number. To do that, you add a data type to your varible decleration
```
int myVar = 5
```
In the example above, I'm telling the computer the I'm going to store an integer (-2,147,483,648 to 2,147,483,647). The computer assines 4 bytes of RAM to store the information a byte is equale to 8 bits (bit / eight = bight or byte). A bit is either a one or a zero. 
This means an int has 32 (4*8) bits and can store 2^32 different numbers. You may notice 2^32 = 4,294,967,296 and not 2,147,483,647. This is becasue you also need to represent all the negitie numbers two.



Data types vary from language to langage so the java spesific ones will be coverd in a differnt "lesson". As a genneral rule of thumb though all languages have a:

* boolean: true or false value (1 bit)
* short: less numbers then an int (16-bit)
* int: normal amount of nombers (32-bit)
* long: more then an int (64-bit)
* float: same as int but with decimals (32-bit)
* double: same as float but with more decimal places (64-bit)
* char: one letter typicly incoded with Unicode (16-bit) 

Two things to note. You can only store decimals in floats and doubles.

```
int myVar = 3
int myOtherVar = 6
int theAnswer = myVar/myOtherVar
```
In the example above, 3/6 is equal to 0.5. But and int can't hold a decimal, so you would get a wrong answer.
The correct way to do it would be:
```
float myVar = 3
float myOtherVar = 6
float theAnswer = myVar/myOtherVar
```
You may be wondering why you I maded the first two varibles floats even though they never hold a decimal value. When you divid two ints, the computer returns an int. To use the two differnt data types, you have to specify you hant a float by adding a (float)\*:
```
int myVar = 3
int myOtherVar = 6
float theAnswer = (float)myVar/(float)myOtherVar
```

Two, you should try to use the best corresponding data type, especially when programing the robot. Lets say you have a varible that you know for sure (if you have even the slightest doubt, go with a bigger type) will never go over a 100. It would be wastfull to use and int or a long, because everytime you did something with that varible, the computer whould need to work with more bytes, thus taking more RAM and posibly more time. In the example given, you should use a short.

\* *Note This is true in java, but not all programing languages*

### Functions
