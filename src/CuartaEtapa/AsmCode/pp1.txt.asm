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
_P11@MAIN_1 DW ?
_P11@MAIN_2 DW ?
_P12@MAIN_1 DW ?
_P12@MAIN_2 DW ?
_P21@MAIN_1 DW ?
_P21@MAIN_2 DW ?
_P22@MAIN_1 DW ?
_P22@MAIN_2 DW ?
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

start:


MOV AX, _P12@MAIN_2
MOV _P11@MAIN_1, AX

MOV AX, 1
MOV _P21@MAIN_1, AX

MOV AX, 2
MOV _P22@MAIN_2, AX

MOV AX, _P21@MAIN_1
MOV _P22@MAIN_1, AX

INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), _P22@MAIN_1

INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), _P22@MAIN_2

end start


