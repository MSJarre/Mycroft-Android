package mycroft.ai.utils

object User{

    init {
        println("Singleton class invoked.")
    }
    var UserName = "UserPIxel"
    fun printVarName(){
        println(UserName)
    }

}

fun main(args: Array<String>) {
    User.printVarName()
    User.UserName = "New Name"
}