.386                  ; Enable 32-bit instructions
.MODEL FLAT,STDCALL           ; Use FLAT memory model for 32-bit systems
.STACK 100h           ; Define stack size;
option casemap :none
include \masm32\include\masm32rt.inc
includelib\masm32\lib\kernel32.lib
includelib\masm32\lib\masm32.lib
dll_dllcrt0 PROTO C
printf PROTO C : VARARG

.data
X@MAIN DW ?
Y@MAIN DW ?
_PAR_DE_UINTEGER@MAIN_1 DW ?
_PAR_DE_UINTEGER@MAIN_2 DW ?
_PAR_DE_SINGLE@MAIN_1 REAL4 ?
_PAR_DE_SINGLE@MAIN_2 REAL4 ?
__new_line__ DB 13, 10, 0 ; CRLF
aux_charch_8  DB "CADENITAAAA ",  0

.code

start:
MOV AX, 3
MOV X@MAIN, AX
MOV AX, 4
MOV Y@MAIN, AX
MOV AX, X@MAIN
MOV _PAR_DE_UINTEGER@MAIN_1, AX
MOV AX, 2
MOV _PAR_DE_UINTEGER@MAIN_2, AX
MOV AX, _PAR_DE_UINTEGER@MAIN_1
MOV _PAR_DE_UINTEGER@MAIN_2, AX
MOV AX, 1
MOV _PAR_DE_UINTEGER@MAIN_1, AX
MOV AX, 2
MOV _PAR_DE_UINTEGER@MAIN_2, AX
INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), _PAR_DE_UINTEGER@MAIN_1
INVOKE printf, addr __new_line__
invoke printf, addr aux_charch_8

end start
