;INCLUYE HEADER Y DATA QUE TENDRIA QUE ESTAR SIEMPRE DESDE EL PRINCIPIO
.386
.MODEL flat, stdcall
option casemap :none
include \masm32\include\masm32rt.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\masm32.lib
dll_dllcrt0 PROTO C
printf PROTO C : VARARG

.data
__new_line__ DB 13, 10, 0 ; CRLF
errorOverflowMsg db "ERROR: Overflow detectado!", 0


.code
OverflowHandler:
    invoke StdOut, addr errorOverflowMsg
    invoke ExitProcess, 1   ; tiene que terminar con la ejecucion




