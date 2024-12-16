.386                  ; Enable 32-bit instructions
.MODEL FLAT,STDCALL           ; Use FLAT memory model for 32-bit systems
.STACK 100h           ; Define stack size;
option casemap :none
include\masm32\include\masm32rt.inc
includelib\masm32\lib\kernel32.lib
includelib\masm32\lib\masm32.lib
dll_dllcrt0 PROTO C
printf PROTO C : VARARG

.data
PARAMETRO@MAIN DW ?
@FUNCION2@MAIN DW ?
@FUNCION1@MAIN@FUNCION2 DW ?
__new_line__ DB 13, 10, 0 ; CRLF
errorOverflowMul db "ERROR: Overflow detectado! Una multiplicacion de enteros excede el limite de 16 bits", 0
errorOverflowSub db "ERROR: Overflow detectado! Una resta de enteros da negativo", 0
errorRecursiveAttempt db "ERROR: Llamado recursivo detectado! No se permite la recursion directa ni indirecta.", 0
chk_rec BYTE 0
PARAMETRO@MAIN@FUNCION2 DW ?
PARAM@MAIN@FUNCION2@FUNCION1 DW ?
auxt_2 DW ?
auxt_7 DW ?
auxt_13 DW ?

.code
OverflowMul:
invoke StdOut, addr errorOverflowMul
invoke ExitProcess, 1   ; tiene que terminar con la ejecucion
OverflowSub:
invoke StdOut, addr errorOverflowSub
invoke ExitProcess, 1   ; tiene que terminar con la ejecucion
RecursiveAttempt:
invoke StdOut, addr errorRecursiveAttempt
invoke ExitProcess, 1   ; tiene que terminar con la ejecucion

FUNCION1@MAIN@FUNCION2 PROC 

MOV AX, PARAMETRO@MAIN@FUNCION2
ADD AX, 1
MOV auxt_2, AX

MOV AX, auxt_2
MOV PARAMETRO@MAIN@FUNCION2, AX

MOV AX, PARAMETRO@MAIN@FUNCION2
MOV @FUNCION1@MAIN@FUNCION2, AX

ret

ret
FUNCION1@MAIN@FUNCION2 ENDP

FUNCION2@MAIN PROC 


MOV AX, PARAMETRO@MAIN@FUNCION2
ADD AX, 1
MOV auxt_7, AX

MOV AX, auxt_7
MOV PARAMETRO@MAIN@FUNCION2, AX

MOV chk_rec, 0
CMP chk_rec, 0
JNZ RecursiveAttempt
MOV AX, PARAMETRO@MAIN@FUNCION2
MOV PARAM@MAIN@FUNCION2@FUNCION1, AX
call FUNCION1@MAIN@FUNCION2

MOV AX, @FUNCION1@MAIN@FUNCION2
MOV @FUNCION2@MAIN, AX

ret

ret
FUNCION2@MAIN ENDP

start:



MOV AX ,2
MOV CX ,1
MOV DX, 0
MUL CX
CMP DX, 0
JNE OverflowMul
MOV auxt_13 ,AX

MOV AX, auxt_13
MOV PARAMETRO@MAIN, AX

MOV chk_rec, 0
CMP chk_rec, 0
JNZ RecursiveAttempt
MOV AX, PARAMETRO@MAIN
MOV PARAMETRO@MAIN@FUNCION2, AX
call FUNCION2@MAIN

INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), @FUNCION2@MAIN

invoke printf, addr __new_line__

end start


