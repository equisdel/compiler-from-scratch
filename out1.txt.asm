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
DC@MAIN DW ?
DB@MAIN DW ?
DA@MAIN DW ?
__new_line__ DB 13, 10, 0 ; CRLF
errorOverflowMul db "ERROR: Overflow detectado! Una multiplicacion de enteros excede el limite de 16 bits", 0
errorOverflowSub db "ERROR: Overflow detectado! Una resta de enteros da negativo", 0
errorRecursiveAttempt db "ERROR: Llamado recursivo detectado! No se permite la recursion directa ni indirecta.", 0
chk_rec BYTE 0
auxt_2 DW ?

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

start:


MOV AX, 1
MOV DA@MAIN, AX

INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), DA@MAIN

MOV AX ,DA@MAIN
MOV CX ,5
MUL CX
CMP DX, 0
JNE OverflowMul
MOV auxt_2 ,AX

MOV AX, auxt_2
MOV DB@MAIN, AX

INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), DB@MAIN

invoke printf, addr __new_line__

end start


