package view;

import buscador.Buscador;
import util.IO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewBuscaManual {
    private JButton btn_buscar;
    private JTextArea txa_result;
    private JTextField txf_nome;
    private JComboBox cmb_sexo;
    private JTextField txt_nome_mae;
    private JPanel pnl_main;
    private JTable tbl_result;
    private JScrollPane scp_result;
    private JTextField txf_dta_nasc;
    private JTextField txf_munic_res;
    private JLabel lbl_nome;
    private JLabel lbl_nome_mae;
    private JLabel lbl_dta_nasc;
    private JLabel lbl_munic_res;
    private JLabel lbl_sexo;

    private ViewBuscaManual() {
        btn_buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                buscar();
            }
        });
        txf_nome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                buscar();
            }
        });
        txt_nome_mae.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                buscar();
            }
        });
        txf_dta_nasc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                buscar();
            }
        });
        txf_munic_res.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                buscar();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Busca Manual");
        frame.setContentPane(new ViewBuscaManual().pnl_main);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        //frame.getRootPane().setDefaultButton(btn_buscar);
    }

    private void createUIComponents() {
        /*
        DateFormat format = new SimpleDateFormat("dd-MMMM-yyyy");
        DateFormatter df = new DateFormatter(format);
        fmt_dta_nac = new JFormattedTextField(df);
        MaskFormatter mask = null;
        try {
            mask = new MaskFormatter("#######");
            mask.setPlaceholderCharacter('_');
        } catch (ParseException e) {
            e.printStackTrace();
        }
        fmt_munic_res = new JFormattedTextField(mask);
        */
        String [] colunas = {"NOME_CAD", "NOME_MAE_CAD", "DTA_NASC_CAD", "SEXO", "MUNIC_CAD", "NIS", /*"COD_FAM", "2011",*/ "SCORE"};
        DefaultTableModel tbl_model = new DefaultTableModel(colunas, 0);
        tbl_result = new JTable(tbl_model);
    }

    private void buscar(){
        // INDEX DECLARATION
        HashMap<String, Point> columnIndex = new HashMap<>();
        /*
        columnIndex.put("NOME", new Point(0, 3));
        columnIndex.put("NOME_MAE", new Point(2, 6));
        columnIndex.put("DATA_NASC", new Point(1, 5));
        columnIndex.put("COD_IBGE", new Point(3, 9));
        columnIndex.put("COD_SEXO", new Point(4, 4));
        */
        columnIndex.put("NOME", new Point(0, 4));
        columnIndex.put("NOME_MAE", new Point(2, 5));
        columnIndex.put("DATA_NASC", new Point(1, 6));
        columnIndex.put("COD_IBGE", new Point(3, 11));
        columnIndex.put("COD_SEXO", new Point(4, 7));

        // HEADER DECLARATION
        String[] header = {"NISES", "NUM_NIS_PESSOA_ATUAL", "NU_NIS_ORIGINAL", "COD_FAMILIAR_FAM", "NOM_PESSOA", "NOM_COMPLETO_MAE_PESSOA", "DTA_NASC_PESSOA", "COD_SEXO_PESSOA", "COD_PARENTESCO_RF_PESSOA", "COD_IBGE_MUNIC_NASC_PESSOA", "DAT_CADASTRAMENTO_FAM", "munic_​res", "Status", "A2007", "A2008", "A2009", "A2011", "A2012"};
        //String[] header = {"NUM_NIS_PESSOA_ATUAL", "NU_NIS_ORIGINAL", "COD_FAMILIAR_FAM", "NOM_PESSOA", "COD_SEXO_PESSOA", "DTA_NASC_PESSOA", "NOM_COMPLETO_MAE_PESSOA", "COD_IBGE_MUNIC_NASC_PESSOA", "COD_EST_CADASTRAL_MEMB", "COD_MUNIC_IBGE"};
        // OBJECTS USED
        IO io = new IO();
        Buscador buscador = null;
        try {
            buscador = new Buscador("/Data/ProjetoCAD/coorte_indexada", header);
            //buscador = new Buscador("assets/cadu2011bahia", header);
            //buscador = new Buscador("indice", header);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // GETTING NAME
        String str_busca = "";
        /*str_busca = "+NOM_PESSOA:\"" + "GEORGE CAIQUE GOUVEIA BARBOSA" + "\" " +
                "+DTA_NASC_PESSOA:\"" + "1993-03-28" + "\" " +
                "+NOM_COMPLETO_MAE_PESSOA:\"" + "VALDECI DOS SANTOS BARBOSA" + "\" " +
                "+munic_​res:" + "291000" + "~1 "+
                "+COD_SEXO_PESSOA:\"" + "1" + "\" ";
        */
        if (!txf_nome.getText().isEmpty()) {
            str_busca = "NOM_PESSOA:(" + buscador.addTermsToString(txf_nome.getText().split(" "), "~") + ") ";
        }
        if (!txt_nome_mae.getText().isEmpty()) {
            str_busca = str_busca + "NOM_COMPLETO_MAE_PESSOA:(" + buscador.addTermsToString(txt_nome_mae.getText().split(" "), "~") + ") ";

        }
        if (!txf_dta_nasc.getText().isEmpty() && txf_dta_nasc.getText().length()==10) {
            str_busca = str_busca + "DTA_NASC_PESSOA:("
                    + txf_dta_nasc.getText().substring(0,4) + "\\-"
                    + txf_dta_nasc.getText().substring(5,7) + "\\-"
                    + txf_dta_nasc.getText().substring(8,10) + ") ";
        }
        if (!txf_munic_res.getText().isEmpty()) {
            str_busca = str_busca + "COD_MUNIC_IBGE:(" + txf_munic_res.getText() + "~) ";
        }
        if (cmb_sexo.getSelectedIndex() != 0) {
            str_busca = str_busca + "COD_SEXO_PESSOA:" + String.valueOf(cmb_sexo.getSelectedIndex());
        }

        if (!str_busca.isEmpty()) {
            JOptionPane.showMessageDialog(null, str_busca);

            DefaultTableModel model = (DefaultTableModel) tbl_result.getModel();
            model.setRowCount(0);

            try {
                ArrayList<String[]> result = buscador.scoreDocToStr(buscador.buscar(str_busca, 100000));
                for (Object[] obj : io.getTextFromResult(result, columnIndex)) {
                    model.addRow(obj);
                }
                //tbl_result.add(io.getTextFromResult(result, columnIndex));
            } catch (org.apache.lucene.queryparser.classic.ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
