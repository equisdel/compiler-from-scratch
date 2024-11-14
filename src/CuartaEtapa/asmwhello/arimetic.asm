.386
.model flat, stdcall
option casemap :none

includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
include \masm32\include\masm32rt.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\masm32.lib

; Operaciones aritméticas

.data
;; 

; SUMA -> ['+',op1,op2] donde op1={CTE,ID,INV_FUNCION,ACC_PAIR,TERCETO (varaux)}
;[3] Terceto{operacion='SUMA', op1='U1', op2='U2', subtipo='UINTEGER'}
MOV AX, op1


