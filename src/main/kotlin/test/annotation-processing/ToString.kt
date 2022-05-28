package test.`annotation-processing`


@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class ToString(
    val include: Array<String> = [], val exclude: Array<String> = []
)
