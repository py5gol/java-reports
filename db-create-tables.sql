CREATE TABLE tb_relatorio (
    id_relatorio serial,
    titulo character varying(200),
    nome character varying(250),
    arquivo bytea,
    arquivo_jrxml bytea,
    id_relatorio_mestre integer
);

CREATE TABLE tb_relatorio_parametro (
    id_parametro serial,
    id_relatorio integer,
    nome character varying(100),
    tipo character varying(30),
    tipo_html character varying(50),
    label_html character varying(50),
    classe character varying(250),
    metodo character varying(100),
    propriedade_chave character varying(50),
    propriedade_descricao character varying(50)
);
