package core.ir;

import core.dataset.TextDataset;
import document.SimilarityMatrix;
import document.TermDocumentMatrix;

/**
 * Created by niejia on 15/2/23.
 */
public interface IRModel {
    public SimilarityMatrix Compute(TextDataset textDataset);

    public TermDocumentMatrix getTermDocumentMatrixOfQueries();

    public TermDocumentMatrix getTermDocumentMatrixOfDocuments();
}
