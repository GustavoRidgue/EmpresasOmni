package br.com.qbrada.qbrada.controller;

import br.com.qbrada.qbrada.model.Evento;
import br.com.qbrada.qbrada.repository.EventoRepository;
import br.com.qbrada.qbrada.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class EventoController {

    @Autowired
    private EventoRepository repository;

    @Autowired
    private EventoService service;

    @GetMapping
    public String index(){
        return "index";
    }

    @GetMapping("/cadastro")
    public String cadastrar() {
        return "cadastro";
    }

    @PostMapping("/cadastrar")
    public String cadastrarEvento(@Valid Evento evento) {
        service.cadastrarEvento(evento);
        return "cadastro";
    }

    @ExceptionHandler({BindException.class})
    public String tratarErrosValidacao(BindException exception, Model model) {
        model.addAttribute("erros", exception.getFieldErrors());
        return "cadastro";
    }

    @GetMapping("/eventos")
    public ModelAndView listarEventos() {
        ModelAndView pagina = new ModelAndView("listarEventos");
        Iterable<Evento> eventos = service.listarEventos();
        pagina.addObject("eventos", eventos);
        return pagina;
    }

    @PostMapping("**/evento")
    public ModelAndView pesquisa(@RequestParam ("nomepesquisa") String nomepesquisa ) {
        ModelAndView mdv = new ModelAndView("listarPorNome");
        mdv.addObject("eventos", repository.findEventoByNome(nomepesquisa));
        return mdv;

    }

    @GetMapping("evento")
    public String buscarNome(@RequestParam("nome") String nome, Model model) {
        Evento evento = service.buscarNome(nome);
        if(evento != null){
            model.addAttribute("evento", evento);
            return "listarEvento";
        }
        else{
            model.addAttribute("msg", "O evento " + nome + " não foi encontrado!! procure novamente");
            return "buscaNome";
        }
    }

    @GetMapping("evento/{nome}")
    public String buscarNomePagina(@PathVariable("nome") String nome, Model model) {
        Evento evento = service.buscarNome(nome);
        if(evento != null){
            model.addAttribute("evento", evento);
            return "listarEvento";
        }
        else{
            model.addAttribute("msg", "O evento " + nome + " não foi encontrado!! procure novamente");
            return "buscaNome";
        }
    }

    @GetMapping("/excluir/{id}")
    public String excluirFilme(@PathVariable("id") long id, Model model){
        service.excluirEvento(id);
        model.addAttribute("excluir", "Um item foi excluido");
        return "redirect:/eventos";
    }

    @GetMapping("/desativar/{id}")
    public String desativar(@PathVariable("id") long id, Model model){
        service.desativar(id);
        model.addAttribute("desativar", "Um item foi desativado");
        return "redirect:/eventos";
    }

}
