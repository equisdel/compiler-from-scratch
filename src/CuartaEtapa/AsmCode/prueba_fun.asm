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
msgdebug db "entro a funcion1", 10, 0
parametro DW 10
aux_funcion1@MAIN WORD ?

.code
funcion1@MAIN proc _x_funcion1@MAIN:WORD
	invoke printf, cfm$("%u\n"), _x_funcion1@MAIN
	invoke StdOut, addr msgdebug
	mov ax, _x_funcion1@MAIN	; el resultado
ret 
funcion1@MAIN endp


start:
invoke funcion1@MAIN, parametro
mov aux_funcion1@MAIN, ax
invoke printf, cfm$("%u\n"), eax
invoke ExitProcess, 0
end start

;SI LA FUNCION DEVUELVE SINGLE (FLOAT) SE USA EAX (32bits)
; SI LA FUNCION DEVUELVE ENTERO SE USA AX


;invoke SendMessage, [hWnd], WM_CLOSE, 0, 0
;Becomes:
; push 0
; push 0
; push WM_CLOSE
; push [hWnd]
; call [SendMessage]