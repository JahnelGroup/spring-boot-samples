package com.jahnelgroup.todo

import org.hibernate.annotations.CreationTimestamp
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.servlet.http.HttpServletRequest

@Controller
@SpringBootApplication
class TodoApplication(var todoRepo: TodoRepo) {

	@ModelAttribute	fun todoRepo() = todoRepo

	@GetMapping("/")	fun index() = "index"

	@RequestMapping(method = [GET, POST])
	fun save(model: Model, req: HttpServletRequest, @ModelAttribute todo: Todo?): String{
		if( req.method == RequestMethod.POST.name ){
			todoRepo.save(todo!!)
			return "index"
		}
		model.addAttribute("todo", Todo())
		return "create"
	}

	@GetMapping("/delete") fun delete(id: Int): String{
		todoRepo.deleteById(id)
		return "index"
	}

	@GetMapping("/detail") fun detail(model: Model, id: Int): String{
		model.addAttribute("todo", todoRepo.deleteById(id))
		return "detail"
	}

	@GetMapping("/edit")	fun edit(model: Model, id: Int): String{
		model.addAttribute("todo", todoRepo.findById(id).get())
		return "edit"
	}
}

@Entity
@Table(name = "todolist")
data class Todo (
		@field:Id
		@field:GeneratedValue()
		var id: Int? = null,
		var todoTitle: String? = null,
		var todoDescription: String? = null,
		@field:CreationTimestamp
		var date: Date? = Date()
)

interface TodoRepo : CrudRepository<Todo, Int>

fun main(args: Array<String>) {
	runApplication<TodoApplication>(*args)
}