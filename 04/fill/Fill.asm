// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.





  //LOOP:
  //key = KBD
	//addr = SCREEN
  //  if key != 0
  //   @BLACK
  //  else
  //    @WHITE
  //  key = RAM[KBD]
  //  goto LOOP

  // (BLACK)
  //  RAM[SCREEN] = -1

  // (WHITE)
  //  RAM[SCREEN] = 0



  (LOOP)
	  @KBD
	  D=M
	  @key
	  M=D // key = KBD

	  @SCREEN
	  D=A
	  @addr
	  M=D // addr = 16384

  @key
  D=M
  @BLACK
  D                             ;JNE

  @WHITE
  0                             ;JMP


  (BLACK) //set black to screen
  @i
  M=0
  @8191
  D=A
  @n
  M=D
  (SETBLACK)
  @i
  D=M
  @n
  D=D-M
  @LOOP //if i > n goto LOOP
  D                             ;JGT  

  @addr
  A=M
  M=-1


  @i  // i = i + 1
  M=M+1
  @addr
  M=M+1

  @SETBLACK
  0                             ;JMP


  (WHITE) // set white to screen
	  @i
	  M=0
	  @8191
	  D=A
	  @n
	  M=D
	  (SETWHITE)
	  @i
	  D=M
	  @n
	  D=D-M
	  @LOOP //if i > n goto LOOP
	  D                             ;JGT  

	  @addr
	  A=M
	  M=0


	  @i  // i = i + 1
	  M=M+1
	  @addr
	  M=M+1

	  @SETWHITE
	  0                             ;JMP

