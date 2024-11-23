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
Z@MAIN DW ?
@A@MAIN DW ?
@B@MAIN@A DW ?
__new_line__ DB 13, 10, 0 ; CRLF
errorOverflowMul db "ERROR: Overflow detectado! Una multiplicacion de enteros excede el limite de 16 bits", 0
errorOverflowSub db "ERROR: Overflow detectado! Una resta de enteros da negativo", 0
errorRecursiveAttempt db "ERROR: Llamado recursivo detectado! No se permite la recursion directa ni indirecta.", 0
chk_rec BYTE 0

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

A@MAIN PROC X@MAIN@A:WORD


MOV chk_rec, 0
CMP chk_rec, 0
JNZ RecursiveAttempt
invoke @B@MAIN@A, X@MAIN@A

MOV AX, @B@MAIN@A
MOV X@MAIN@A, AX

MOV AX, X@MAIN@A
MOV @A@MAIN, AX

ret

ret
A@MAIN ENDP

B@MAIN@A PROC Y@MAIN@A@B:WORD

MOV AX, 10
MOV Z@MAIN, AX

MOV AX, Z@MAIN
MOV @B@MAIN@A, AX

ret

ret
B@MAIN@A ENDP

start:



MOV AX, 0
MOV Z@MAIN, AX

MOV chk_rec, 0
CMP chk_rec, 0
JNZ RecursiveAttempt
invoke A@MAIN, Z@MAIN

MOV AX, @A@MAIN
MOV Z@MAIN, AX

INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), Z@MAIN

end start


