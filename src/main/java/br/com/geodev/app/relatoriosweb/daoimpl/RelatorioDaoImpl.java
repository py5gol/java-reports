package br.com.geodev.app.relatoriosweb.daoimpl;

import br.com.geodev.app.relatoriosweb.bean.Relatorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RelatorioDaoImpl {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    public List<Relatorio> findAll ()
    {
        return (List<Relatorio>) hibernateTemplate.find("from Relatorio");
    }

    public void insert(Relatorio relatorio)
    {
        hibernateTemplate.persist(relatorio);
    }

    public void delete(Relatorio relatorio)
    {
        hibernateTemplate.delete(relatorio);
    }

    public Relatorio findById(Long id)
    {
        List<Relatorio> relatorios = hibernateTemplate.find("from Relatorio e where e.id = ?", id.intValue());

        if(relatorios != null && !relatorios.isEmpty())
            return relatorios.get(0);

        return null;
    }
    
}
