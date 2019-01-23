package com.jahnelgroup.todo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestMethod.*
import javax.servlet.http.HttpServletRequest

@Controller
@SpringBootApplication
class TodoApplication(var todoRepo: TodoRepo){
	@ModelAttribute fun todoRepo() = todoRepo

	@GetMapping("/")
	fun index() = "index"

	@RequestMapping(method = [GET, POST])
	fun create(model: Model, req: HttpServletRequest, @ModelAttribute todo: Todo?): String{
		if( req.method == RequestMethod.POST.name ){
			todoRepo.save(todo!!)
			return "index"
		}
		model.addAttribute("todo", Todo())
		return "create"
	}

}

fun main(args: Array<String>) {
	runApplication<TodoApplication>(*args)
}

