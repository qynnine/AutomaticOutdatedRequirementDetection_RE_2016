package exp.Aqualush.all_changes;

import core.dataset.TextDataset;
import core.metrics.Result;
import core.metrics.cut.ConstantThresholdResult;
import document.LinksList;
import document.SimilarityMatrix;
import document.SingleLink;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Created by niejia on 16/1/18.
 */
public class MergeResult {
    private SimilarityMatrix mergedSimilarity;
    private SimilarityMatrix mergedOracle;

    public MergeResult(Set<String> changesVersion, Map<String, Result> changesResult, Map<String, TextDataset> changesTextDataset) {
        mergedSimilarity = new SimilarityMatrix();
        mergedOracle = new SimilarityMatrix();

        for (String changeVersion : changesVersion) {
            Result result = changesResult.get(changeVersion);
            TextDataset textDataset = changesTextDataset.get(changeVersion);
            SimilarityMatrix matrix = result.getMatrix();
            SimilarityMatrix oracle = result.getOracle();

            int maxScore = textDataset.getTargetCollection().size();

            for (String source : matrix.sourceArtifactsIds()) {
                int currentCut = maxScore;
                LinksList linksList = new LinksList();
                Map<String, Double> links = matrix.getLinksForSourceId(source);
                for (String target : links.keySet()) {
                    linksList.add(new SingleLink(source, target, links.get(target)));
                }

                Collections.sort(linksList, Collections.reverseOrder());

                for (SingleLink link : linksList) {
                    mergedSimilarity.addLink(link.getSourceArtifactId(), link.getTargetArtifactId(), (double) currentCut);
                    currentCut--;
                }
            }

            for (String source : oracle.sourceArtifactsIds()) {
                Map<String, Double> links = oracle.getLinksForSourceId(source);
                for (String target : links.keySet()) {
                    mergedOracle.addLink(source, target, links.get(target));
                }
            }
        }

        System.out.println("No way");
    }

    public Result getMergedResult() {
        return new ConstantThresholdResult(mergedSimilarity, mergedOracle);
    }
}
