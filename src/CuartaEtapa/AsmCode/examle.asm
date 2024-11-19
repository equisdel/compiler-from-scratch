; Example of MASM Assembly Program using PROC
; This program calculates the sum of two numbers using a procedure.

.MODEL SMALL         ; Define the memory model
.STACK 100h          ; Define stack size

.DATA
num1 DW 5            ; First number
num2 DW 10           ; Second number
result DW ?          ; Variable to store the result

.CODE
MAIN PROC
    ; Initialize the data segment
    MOV AX, @DATA
    MOV DS, AX

    ; Call the AddNumbers procedure
    MOV AX, num1       ; Pass the first number in AX
    MOV BX, num2       ; Pass the second number in BX
    CALL AddNumbers    ; Call the procedure
    MOV result, AX     ; Store the result in the 'result' variable

    ; Exit program
    MOV AH, 4Ch        ; DOS interrupt to terminate the program
    INT 21h
MAIN ENDP

; Function to add two numbers
AddNumbers PROC
    ; Input: AX = first number, BX = second number
    ; Output: AX = result of addition
    ADD AX, BX          ; Add BX to AX
    RET                 ; Return to the caller
AddNumbers ENDP

END MAIN                ; End of program
