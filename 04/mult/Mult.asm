// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)

// Put your code here.



//a=RAM[0], b=RAM[1]
//c=0
//i=1
//LOOP:
  // if i > a goto STOP
  //  c = c + b
  //  i = i + 1
  //goto LOOP
//STOP:
//RAM[2] = c



  //Muitiplies R0 and R1
  //Usage: put a number a in R0 and a number b in R1
  @R0
  D=M
  @a
  M=D // a = R0
  @R1
  D=M
  @b
  M=D // b = R1
  @i
  M=1
  @c
  M=0
(LOOP)
  @i
  D=M
  @b
  D=D-M
  @STOP
  D;JGT // if i > b goto STOP
  @a
  D=M
  @c
  M=M+D // c = c + a
  @i
  M=M+1 // i = i + 1
  @LOOP
  0;JMP
(STOP)
  @c
  D=M
  @R2
  M=D // RAM[2] = c
(END)
  @END
  0                             ;JMP
