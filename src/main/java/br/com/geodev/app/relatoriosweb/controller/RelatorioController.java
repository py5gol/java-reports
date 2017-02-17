package br.com.geodev.app.relatoriosweb.controller;

import br.com.geodev.app.relatoriosweb.daoimpl.RelatorioDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by IntelliJ IDEA.
 * User: robertosantos
 * Date: 01/12/2010
 * Time: 23:59:13
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class RelatorioController {

    @Autowired
    private RelatorioDaoImpl relatorioDao;

    @RequestMapping(value = "/relatorios")
    public ModelAndView index(String msg, String cor) {

        ModelAndView mav = new ModelAndView("relatorios");

        mav.addObject("relatorios", relatorioDao.findAll());

        if(msg != null)
        {
            mav.addObject("mensagem", msg);
            mav.addObject("cor", cor);
        }

        return mav;
    }

    @RequestMapping(value="/relatorio/{id}")
    public ModelAndView relatorioEditar(@PathVariable("id") Long id) {

        ModelAndView mav = new ModelAndView("relatorioEditar");

        mav.addObject("relatorio", relatorioDao.findById(id));

        return mav;
    }

    @RequestMapping(value = "/secure/empreendedor/listar")
    public ModelAndView list(String msg, String cor) {

        ModelAndView mav = new ModelAndView("secure/listempreendedor");
        mav.addObject("empreendedores", relatorioDao.findAll());

        if(msg != null)
        {
            mav.addObject("mensagem", msg);
            mav.addObject("cor", cor);
        }

        return mav;
    }

    @RequestMapping(value = "/secure/empreendedor/alterar/{id}")
    public ModelAndView alterar(@PathVariable("id") Long id) {

        ModelAndView mav = new ModelAndView("secure/listempreendedor");

        mav.addObject("empreendedores", relatorioDao.findAll());

        return mav;
    }

    @RequestMapping(value = "/secure/empreendedor/excluir/{id}", method= RequestMethod.GET)
    public ModelAndView excluir(@PathVariable("id") Long id) {

        relatorioDao.delete(relatorioDao.findById(id));

        return list("Empreendedor excluido com sucesso", "green");
    }

    @RequestMapping(value = "/secure/empreendedor/cadastrar", method = RequestMethod.POST)
    public ModelAndView cadastrar(@RequestParam("nome") String nome, @RequestParam("cnpj") String cnpj,
            @RequestParam("endereco") String endereco, @RequestParam("numero") String numero,
            @RequestParam("complemento") String complemento, @RequestParam("municipio") Integer municipio,
            @RequestParam("observacao") String observacao, @RequestParam("nome_contato") String nomeContato,
            @RequestParam("tipo_telefone") Integer tipoTelefone, @RequestParam("tel_area") Integer telArea,
            @RequestParam("tel_numero") Long telNumero, @RequestParam("email") String email) {

        /*
        Empreendedor e = new Empreendedor();
        e.setNome(nome);
        e.setCnpj(cnpj);
        e.setEndereco(endereco);
        e.setNumero(numero);
        e.setComplemento(complemento);
        e.setMunicipio(municipioDao.findById(municipio));
        e.setObservacao(observacao);

        Email em = new Email();
        em.setNome(email);

        Telefone t = new Telefone();
        t.setArea(telArea);
        t.setNumero(telNumero);
        t.setTipoTelefone(tipoTelefoneDao.findById(tipoTelefone));

        Contato c = new Contato();
        c.setNome(nomeContato);
        c.setEmail(em);
        c.setTelefone(t);

        List<Contato> contatos = new ArrayList<Contato>();
        contatos.add(c);

        e.setContatos(contatos);

        empreendedorDao.insert(e);
        */
        return index("Empreendedor cadastrado com sucesso", "green");
    }

}
