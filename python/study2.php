<?php

$v8 = 'hello';

try {
  var_dump($v8->executeString("return 'Hello';"));
} catch (V8JsException $e) {
  var_dump($e);
}

?>