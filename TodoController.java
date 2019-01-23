package com.gm.springboot.web.TestApp.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gm.springboot.web.TestApp.Service.TodoRepository;

import com.gm.springboot.web.TestApp.model.Todo;

@Controller
public class TodoController {
		
		@Autowired
		TodoRepository repository;
		@InitBinder
		protected void initBinder(WebDataBinder binder) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			binder.registerCustomEditor(Date.class, new CustomDateEditor(
					dateFormat, false));
		}
		@RequestMapping(value ="/list-todos",method=RequestMethod.GET)
		public String ShowTodos( ModelMap model) {
			String name = getLoggedInUserName(model);
			model.put("todos",repository.findByUser(name));
			return "list-todos";
		}
		private String getLoggedInUserName(ModelMap model) {
			Object principal =
					SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(principal instanceof UserDetails) {
				return ((UserDetails)principal).getUsername();	
			}
			return principal.toString();
		}
		
		@RequestMapping(value ="/add-todo",method=RequestMethod.GET)
		public String addTodos( ModelMap model) {
			model.addAttribute("todo", new Todo(0,getLoggedInUserName(model),"Default desc",new Date(),false));
			return "todo";
		}
		@RequestMapping(value ="/add-todo",method=RequestMethod.POST)
		public String ShowaddTodoPage( ModelMap model,@Valid Todo todo,BindingResult result) {
			if(result.hasErrors()) {
				return "todo";
			}
			
			todo.setUser(getLoggedInUserName(model));
			repository.save(todo);
			//service.addTodo(getLoggedInUserName(model), todo.getDesc(),todo.getTargetDate(), false);
			return "redirect:/list-todos";
		}
		@RequestMapping(value ="/delete-todo",method=RequestMethod.GET)
		public String deleteTodoPage(@RequestParam int id) {
			repository.deleteById(id);
			return "redirect:/list-todos";
		}
		@RequestMapping(value ="/update-todo",method=RequestMethod.GET)
		public String ShowupdateTodoPage(@RequestParam int id,ModelMap model) {
			Optional<Todo> todo = repository.findById(id);
			model.put("todo", todo);
			return "todo";
		}
		@RequestMapping(value ="/update-todo",method=RequestMethod.POST)
		public String updateTodoPage(ModelMap model, @Valid Todo todo, BindingResult result) {
			
			if(result.hasErrors()) {
				return "todo";
			}
			todo.setUser(getLoggedInUserName(model));
			repository.save(todo);
			return "redirect:/list-todos";
		}
	}
