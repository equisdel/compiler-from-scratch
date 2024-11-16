(module

(import "console" "log" (func $log (param i32 i32)))
  (import "js" "mem" (memory 1))
  (data (i32.const 101) "Holabuenatarde")  
;; loop
;; int a = 1; 
;; while (i > 0) {
;;   a = a * i;
;;   i = i - 1;
;; }
(func $f7 (param i64) (result i64)
 (local i64)
 i64.const 1 ;; pone un 1 en la pila (inicializa a en 1)
 local.set 1 ;; guarda tope de pila en local (index 1)
 (block
           local.get 0 ;; es el indice que va a ir restando hasta 0 (i)
           ;;toma el valor de la variable local en index 0 y lo pone en pila
           ;; EN EL INDICE 0 DE LA VARAIBLE LOCAL, SE ENCEUNTRA EL PARAMETRO QUE LE PASAN A LA FUNCION
           i64.eqz
           br_if 0  ;; si es cero no entra al loop, salta
           (loop
           i32.const 101  ;; ref a memoria? imprime el string
           i32.const 14  ;; cantidades de caracteres del string a imprimir
           call $log ;; imprime en consola, pasa el string y el offset (funcion hecha en JS)
           local.get 1  ;; pongo en pila a
           local.get 0 ;; pongo en pila i
           i64.mul
           local.set 1 ;; pongo resultado en a
           local.get 0 ;; pongo en pila i
           i64.const -1 ;; pongo en pila -1
           i64.add  ;; i - 1
           local.set 0 ;; guardo en i
           local.get 0  ;; pongo en pila i
           ;;local.tee 0
           i64.eqz  ;; si tope pila es 0
           br_if 1  ;; afueraaa
           br 0))
       local.get 1)

(func $main (result i32)    
    (local $w i32)
    (local $s i32)
    i32.const 5
    local.set $w        
    local.get $w      ;; rem f2 f3   
    i64.extend_i32_s  ;; add f7
    call $f7 
    i32.wrap_i64 ;; add f7
    local.set $s 
    ;;i32.const 101
    ;;i32.const 4
    ;;call $log       
    local.get $s        
    ) 

(export "main"  (func $main))
)

