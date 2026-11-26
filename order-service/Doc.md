## Reglas del negocio

### Carrito de compras

- Solo un carrito activo por usuario.
- El producto debe existir, la cantidad debe ser mayor a cero.
- Un carrito vacio no puede convertirse en orden.
- El cliente solo puede seleccionar el producto y la cantidad.
- Un usuario solo tiene acceso a su carrito.
- Un carrito debe verificar sus productos con preview para convertirse en orden .
- El usuario debe aceptar los cambios devueltos por preview para convertirse en orden.
- Un carrito verificado se convierte en orden, modifica el stock actual y deja registro la orden.

### Ordenes
- Una orden es un registro histórico que no puede ser modificado una vez creado.
- Cada orden pertenece a un usuario, muchas ordenes pueden pertenecer a un usuario
- Un usuario solo puede consultar sus ordenes
- Un admin puede consultar todas las ordenes
- Un reintento de checkout no debe generar ordenes dobles.

## Funcionalidades

### Carrito

- Crear un carrito nuevo o recuperar el carrito por id vinculado al usuario.
- Agregar producto y cantidad.
- Modificar cantidad.
- Eliminar producto.
- Vaciar el carrito.
- Verificación.
- Generación de orden.

### Ordenes

- Consultar todas las ordenes (Solo Admin)
- Consultar todas las ordenes de un usuario. (Admin y Usuario)
- Consultar detalles de una orden (Admin todas, Usuario solo la propia)

## End Points

### Carrito
|Método	|Endpoint			|Descripción							|
|GET	|/api/cart			|Crea un carrito nuevo o recupera los elementos de uno existente		|
|POST	|/api/cart/add			|Agregar productos al carrito 					|
|PUT	|/api/cart/update			|Cambia la cantidad de un producto existente				|
|DELETE	|/api/cart/delete			|Elimina los productos del carrito					|
|DELETE	|/api/cart/empty			|Vacia el carrito							|
|GET	|/api/cart/check			|Verifica que la orden pueda ser creada				|
|POST	|/api/cart/generate			|Genera una orden nueva						|

### Ordenes
|GET	|/api/order/all			|Devuelve todas las ordenes generadas				|
|GET	|/api/order/user			|Devuelve las ordenes de un usuario					|
|GET	|/api/order/details			|Devuelve los detalles de una orden					|

## Modelos cart_db

### carts

- id
- user_id
- status
- created_at

### cart_items

- user_id
- product_id
- product_name
- product_price
- cant
- subtotal

### orders

- id
- user_id
- total
- created_at

### order_items

- order_id
- product_id
- product_name
- product_price
- cant
- subtotal

