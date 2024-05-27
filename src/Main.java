import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.io.BufferedReader;
import java.io.FileReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.StringJoiner;

class Node {
    Node esquerda, direita;
    String valor, conteudo;
    boolean foiCopiado;

    Node(Node esquerda, Node direita, String valor,
         String conteudo, boolean foiCopiado)
    {
        this.esquerda = esquerda;
        this.direita = direita;
        this.valor = valor;
        this.conteudo = conteudo;
        this.foiCopiado = foiCopiado;
    }

    static String hash(String val)
            throws NoSuchAlgorithmException
    {
        MessageDigest digest = MessageDigest.getInstance("Tiger");
        byte[] hash = digest.digest(val.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    Node copiar()
    {
        return new Node(this.esquerda, this.direita, this.valor,
                this.conteudo, true);
    }
}

class MerkleTree {
    Node raiz;

    MerkleTree(List<String> valores)
            throws NoSuchAlgorithmException
    {
        this.raiz = construirArvore(valores);
    }

    Node construirArvore(List<String> valores)
            throws NoSuchAlgorithmException
    {
        List<Node> folhas = new ArrayList<>();
        for (String e : valores) {
            folhas.add(new Node(null, null, Node.hash(e), e, false));
        }
        if (folhas.size() % 2 == 1) {
            folhas.add(folhas.get( folhas.size() - 1).copiar());
        }
        return construirArvoreRec(folhas);
    }

    Node construirArvoreRec(List<Node> nodeList)
            throws NoSuchAlgorithmException
    {
        if (nodeList.size() % 2 == 1) {
            nodeList.add(nodeList.get(nodeList.size() - 1).copiar());
        }
        int metade = nodeList.size() / 2;

        if (nodeList.size() == 2) {
            return new Node(nodeList.get(0), nodeList.get(1),
                    Node.hash(nodeList.get(0).valor + nodeList.get(1).valor),
                    nodeList.get(0).conteudo + "+" + nodeList.get(1).conteudo, false);
        }

        Node esquerda = construirArvoreRec(nodeList.subList(0, metade));
        Node direita = construirArvoreRec(
                nodeList.subList(metade, nodeList.size()));
        String valor = Node.hash(esquerda.valor + direita.valor);
        String conteudo = esquerda.conteudo + "+" + direita.conteudo;
        return new Node(esquerda, direita, valor, conteudo, false);
    }

    void imprimirArvore() { imprimirArvoreRec(this.raiz); }

    void imprimirArvoreRec(Node node)
    {
        if (node != null) {
            if (node.esquerda != null) {
                System.out.println("Esquerda: " + node.esquerda.valor);
                System.out.println("Direita: " + node.direita.valor);
            }
            else {
                System.out.println("Entrada");
            }

            if (node.foiCopiado) {
                System.out.println("(elemento fantasma)");
            }
            System.out.println("Valor: " + node.valor);
            System.out.println("Conteudo: " + node.conteudo);
            System.out.println("");
            imprimirArvoreRec(node.esquerda);
            imprimirArvoreRec(node.direita);
        }
    }

    String getRaizHash() { return this.raiz.valor; }
}

public class Main {
    public static void main(String[] args)
            throws NoSuchAlgorithmException, IOException
    {
        Security.addProvider(new BouncyCastleProvider());

        String caminhoArquivo = "C:\\Users\\gusta\\Desktop\\dataHash.txt";

        List<String> elementos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                elementos.add(linha);
            }
        }


        //System.out.print("Entrada: [");
        //for (String elemento : elementos) {
        //System.out.print(elemento + " ");
        //}
        System.out.println("Entrada: " + new StringJoiner(", ").add(elementos.toString()));
        System.out.println("\n");

        MerkleTree arvore = new MerkleTree(elementos);
        arvore.imprimirArvore();

        System.out.println("Raiz Hash: " + arvore.getRaizHash() + "\n");
    }
}