package test

import kotlin.jvm.functions.FunctionN



@Suppress("UNCHECKED_CAST")
fun Function<*>.call(vararg args: Any?): Any? {
  return when(this) {
    is Function0 -> {
      invoke()
    }
    is Function1<*,*> -> {
      (this as Function1<Any?,Any?>).invoke(args[0])
    }
    is Function2<*,*,*> -> {
      (this as Function2<Any?,Any?,Any?>).invoke(args[0],args[1])
    }
    is Function3<*,*,*,*> -> {
      (this as Function3<Any?,Any?,Any?,Any?>).invoke(args[0],args[1],args[2])
    }
    is Function4<*,*,*,*,*> -> {
      (this as Function4<Any?,Any?,Any?,Any?,Any?>).invoke(args[0],args[1],args[2],args[3])
    }
    is Function5<*,*,*,*,*,*> -> {
      (this as Function5<Any?,Any?,Any?,Any?,Any?,Any?>).invoke(args[0],args[1],args[2],args[3],args[4])
    }
    is Function6<*,*,*,*,*,*,*> -> {
      (this as Function6<Any?,Any?,Any?,Any?,Any?,Any?,Any?>).invoke(args[0],args[1],args[2],args[3],args[4],args[5])
    }
    is Function7<*,*,*,*,*,*,*,*> -> {
      (this as Function7<Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?>).invoke(args[0],args[1],args[2],args[3],args[4],args[5],args[6])
    }
    is Function8<*,*,*,*,*,*,*,*,*> -> {
      (this as Function8<Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?>).invoke(args[0],args[1],args[2],args[3],args[4],args[5],args[6],args[7])
    }
    is Function9<*,*,*,*,*,*,*,*,*,*> -> {
      (this as Function9<Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?>).invoke(args[0],args[1],args[2],args[3],args[4],args[5],args[6],args[7],args[8])
    }
    is Function10<*,*,*,*,*,*,*,*,*,*,*> -> {
      (this as Function10<Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?>).invoke(args[0],args[1],args[2],args[3],args[4],args[5],args[6],args[7],args[8],args[9])
    }
    is Function11<*,*,*,*,*,*,*,*,*,*,*,*> -> {
      (this as Function11<Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?>).invoke(args[0],args[1],args[2],args[3],args[4],args[5],args[6],args[7],args[8],args[9],args[10])
    }
    is Function12<*,*,*,*,*,*,*,*,*,*,*,*,*> -> {
      (this as Function12<Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?>).invoke(args[0],args[1],args[2],args[3],args[4],args[5],args[6],args[7],args[8],args[9],args[10],args[11])
    }
    is Function13<*,*,*,*,*,*,*,*,*,*,*,*,*,*> -> {
      (this as Function13<Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?>).invoke(args[0],args[1],args[2],args[3],args[4],args[5],args[6],args[7],args[8],args[9],args[10],args[11],args[12])
    }
    is Function14<*,*,*,*,*,*,*,*,*,*,*,*,*,*,*> -> {
      (this as Function14<Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?>).invoke(args[0],args[1],args[2],args[3],args[4],args[5],args[6],args[7],args[8],args[9],args[10],args[11],args[12],args[13])
    }
    is Function15<*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*> -> {
      (this as Function15<Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?>).invoke(args[0],args[1],args[2],args[3],args[4],args[5],args[6],args[7],args[8],args[9],args[10],args[11],args[12],args[13],args[14])
    }
    is Function16<*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*> -> {
      (this as Function16<Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?>).invoke(args[0],args[1],args[2],args[3],args[4],args[5],args[6],args[7],args[8],args[9],args[10],args[11],args[12],args[13],args[14],args[15])
    }
    is Function17<*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*> -> {
      (this as Function17<Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?>).invoke(args[0],args[1],args[2],args[3],args[4],args[5],args[6],args[7],args[8],args[9],args[10],args[11],args[12],args[13],args[14],args[15],args[16])
    }
    is Function18<*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*> -> {
      (this as Function18<Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?>).invoke(args[0],args[1],args[2],args[3],args[4],args[5],args[6],args[7],args[8],args[9],args[10],args[11],args[12],args[13],args[14],args[15],args[16],args[17])
    }
    is Function19<*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*> -> {
      (this as Function19<Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?>).invoke(args[0],args[1],args[2],args[3],args[4],args[5],args[6],args[7],args[8],args[9],args[10],args[11],args[12],args[13],args[14],args[15],args[16],args[17],args[18])
    }
    is Function20<*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*> -> {
      (this as Function20<Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?>).invoke(args[0],args[1],args[2],args[3],args[4],args[5],args[6],args[7],args[8],args[9],args[10],args[11],args[12],args[13],args[14],args[15],args[16],args[17],args[18],args[19])
    }
    is Function21<*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*> -> {
      (this as Function21<Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?>).invoke(args[0],args[1],args[2],args[3],args[4],args[5],args[6],args[7],args[8],args[9],args[10],args[11],args[12],args[13],args[14],args[15],args[16],args[17],args[18],args[19],args[20])
    }
    is Function22<*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*,*> -> {
      (this as Function22<Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?,Any?>).invoke(args[0],args[1],args[2],args[3],args[4],args[5],args[6],args[7],args[8],args[9],args[10],args[11],args[12],args[13],args[14],args[15],args[16],args[17],args[18],args[19],args[20],args[21])
    }
    is FunctionN<*> -> {
      (this as FunctionN<Any?>).invoke(*args)
    }
    
    else -> { throw RuntimeException() }
  }
}