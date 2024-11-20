.386                  ; Enable 32-bit instructions
.MODEL FLAT           ; Use FLAT memory model for 32-bit systems
.STACK 100h           ; Define stack size;
option casemap :none
include \masm32\include\masm32rt.inc
includelib\masm32\lib\kernel32.lib
includelib\masm32\lib\masm32.lib
dll_dllcrt0 PROTO C
printf PROTO C : VARARG

.data
X:MAIN DW ?
Y:MAIN DW ?
PAR_DE_UINTEGER@MAIN_1 DW ?
_PAR_DE_UINTEGER@MAIN_2 DW ?
_PAR_DE_SINGLE@MAIN_1 DD ?
_PAR_DE_SINGLE@MAIN_2 DD ?

.code

start:
MOV X@MAIN,3
MOV Y@MAIN,4
MOV _PAR_DE_UINTEGER@MAIN_1,X@MAIN
MOV _PAR_DE_UINTEGER@MAIN_2,2
MOV _PAR_DE_UINTEGER@MAIN_2,_PAR_DE_UINTEGER@MAIN_1
MOV _PAR_DE_UINTEGER@MAIN_1,1
MOV _PAR_DE_UINTEGER@MAIN_2,2

end start
