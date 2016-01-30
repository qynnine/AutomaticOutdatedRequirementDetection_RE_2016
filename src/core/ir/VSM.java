package core.ir;

import core.dataset.TextDataset;
import document.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by niejia on 15/2/23.
 */
public class VSM implements IRModel{

    private TermDocumentMatrix queries;
    private TermDocumentMatrix documents;

    public SimilarityMatrix Compute(TextDataset textDataset) {
        return Compute(new TermDocumentMatrix(textDataset.getSourceCollection()), new TermDocumentMatrix(textDataset.getTargetCollection()));
    }

    private SimilarityMatrix Compute(TermDocumentMatrix source, TermDocumentMatrix target) {
        TermDocumentMatrix IDs = ComputeIdentities(source);

        TermDocumentMatrix TF = ComputeTF(target);
        double[] IDF = ComputeIDF(ComputeDF(target), target.NumDocs());

        TermDocumentMatrix TFIDF = ComputeTFIDF(TF, IDF);
        return ComputeSimilarities(IDs, TFIDF);
    }

    private TermDocumentMatrix ComputeTFIDF(TermDocumentMatrix tf, double[] idf) {
        for (int i = 0; i < tf.NumDocs(); i++) {
            for (int j = 0; j < tf.NumTerms(); j++) {
                tf.setValue(i, j, tf.getValue(i, j) * idf[j]);
            }
        }
        return tf;
    }

    private double[] ComputeIDF(double[] df, int numDocs) {
        double[] idf = new double[df.length];
        for (int i = 0; i < df.length; i++) {
            if (df[i] <= 0.0) {
                idf[i] = 0.0;
            } else {
                idf[i] = Math.log(numDocs / df[i]);
            }
        }
        return idf;
    }

    private double[] ComputeDF(TermDocumentMatrix matrix) {
        double[] df = new double[matrix.NumTerms()];
        for (int j = 0; j < matrix.NumTerms(); j++) {
            df[j] = 0.0;
            for (int i = 0; i < matrix.NumDocs(); i++) {
                df[j] += (matrix.getValue(i, j) > 0.0) ? 1.0 : 0.0;
            }
        }
        return df;
    }

    private TermDocumentMatrix ComputeTF(TermDocumentMatrix matrix) {
        for (int i = 0; i < matrix.NumDocs(); i++) {
            double max = 0.0;
            for (int k = 0; k < matrix.NumTerms(); k++) {
                max += matrix.getValue(i, k);
            }

            for (int j = 0; j < matrix.NumTerms(); j++) {
                matrix.setValue(i, j, (matrix.getValue(i, j) / max));
            }
        }
        return matrix;
    }

    private TermDocumentMatrix ComputeIdentities(TermDocumentMatrix matrix) {
        for (int i = 0; i < matrix.NumDocs(); i++) {
            for (int j = 0; j < matrix.NumTerms(); j++) {
                // Calculate term frequency in query
//                matrix.setValue(i, j, ((matrix.getValue(i, j) > 0.0) ? matrix.getValue(i, j) : 0.0));
                matrix.setValue(i, j, ((matrix.getValue(i, j) > 0.0) ? 1.0 : 0.0));
            }
        }
        return matrix;
    }

    private SimilarityMatrix ComputeSimilarities(TermDocumentMatrix ids, TermDocumentMatrix tfidf) {
        SimilarityMatrix sims = new SimilarityMatrix();
        List<TermDocumentMatrix> matrices = TermDocumentMatrix.Equalize(ids, tfidf);

        queries = matrices.get(0);
        documents = matrices.get(1);

        HashMap<String, String> termScoreInfo = new HashMap<>();

        for (int i = 0; i < ids.NumDocs(); i++) {
            LinksList links = new LinksList();
            for (int j = 0; j < tfidf.NumDocs(); j++) {
                double product = 0.0;
                double asquared = 0.0;
                double bsquared = 0.0;

                StringBuilder termScore = new StringBuilder();

                for (int k = 0; k < matrices.get(0).NumTerms(); k++) {

                    double a = matrices.get(0).getValue(i, k);

                    double b = matrices.get(1).getValue(j, k);

//                    if (b == 0.0) {
//                        continue;
//                    }

                    if (a != 0.0 ) {
//                        System.out.println(matrices.get(0).getTermName(k)+" " + a );
                    }

                    if (a * b > 0) {
                        // Bingo term
                        String bingoTerm = matrices.get(0).getTermName(k);
                        termScore.append(bingoTerm + "(" + (a * b) + ")");
                        termScore.append(a + " " + b);
                        termScore.append("\n");
//                        System.out.println(bingoTerm + "(" + (a * b) + ")");
                    }
                    product += (a * b);
                    asquared += Math.pow(a, 2);
                    bsquared += Math.pow(b, 2);
                }
//                sb.append("----------------------");

                double cross = Math.sqrt(asquared) * Math.sqrt(bsquared);
                if (cross == 0.0) {
                    links.add(new SingleLink(ids.getDocumentName(i).trim(), tfidf.getDocumentName(j).trim(), 0.0));
                } else {
                    links.add(new SingleLink(ids.getDocumentName(i), tfidf.getDocumentName(j), product / cross));
                }

                if (links.getScore(ids.getDocumentName(i), tfidf.getDocumentName(j)) > 0.0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(ids.getDocumentName(i).trim());
                    sb.append(" ");
                    sb.append(tfidf.getDocumentName(j).trim());
                    sb.append(" ");
                    sb.append(links.getScore(ids.getDocumentName(i), tfidf.getDocumentName(j)));
                    sb.append("\n");

                    sb.append(termScore.toString());
                    String linkIdentifer = ids.getDocumentName(i).trim() + "#" + tfidf.getDocumentName(j).trim();
//                    System.out.println(sb.toString());
                    termScoreInfo.put(linkIdentifer, sb.toString());
                }
            }

            Collections.sort(links, Collections.reverseOrder());

            for (SingleLink link : links) {
                sims.addLink(link.getSourceArtifactId(), link.getTargetArtifactId(), link.getScore());
                if (link.getScore() > 0.0) {
                    String id = link.getSourceArtifactId() + "#" + link.getTargetArtifactId();
                    System.out.println(termScoreInfo.get(id));
                }
            }
        }

        return sims;
    }

    @Override
    public TermDocumentMatrix getTermDocumentMatrixOfQueries() {
        return queries;
    }

    @Override
    public TermDocumentMatrix getTermDocumentMatrixOfDocuments() {
        return documents;
    }
}
