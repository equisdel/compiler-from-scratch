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
msgdebug db "entro a funcion1", 0
parametro DW 10

.code
funcion1@MAIN proc _x_funcion1@MAIN:WORD
	invoke printf, cfm$("%u\n"), _x_funcion1@MAIN
	invoke StdOut, addr msgdebug
ret 0
funcion1@MAIN endp

start:
invoke funcion1@MAIN, parametro

invoke ExitProcess, 0
end start