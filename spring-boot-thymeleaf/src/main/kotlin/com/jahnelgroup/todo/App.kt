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
	fun save(model: Model, req: HttpServletRequest, @ModelAttribute todo: Todo?): String{
		if( req.method == RequestMethod.POST.name ){
			todoRepo.save(todo!!)
			return "index"
		}
		model.addAttribute("todo", Todo())
		return "create"
	}

	@GetMapping("/delete")
	fun delete(id: Int): String{
		todoRepo.deleteById(id)
		return "index"
	}

	@GetMapping("/detail")
	fun detail(model: Model, id: Int): String{
		model.addAttribute("todo", todoRepo.deleteById(id))
		return "detail"
	}

	@GetMapping("/edit")
	fun edit(model: Model, id: Int): String{
		model.addAttribute("todo", todoRepo.findById(id).get())
		return "edit"
	}
}

fun main(args: Array<String>) {
	runApplication<TodoApplication>(*args)
}