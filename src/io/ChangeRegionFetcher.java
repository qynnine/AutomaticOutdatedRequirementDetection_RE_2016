package io;

import java.util.HashSet;
import java.util.List;

/**
 * Created by niejia on 16/1/3.
 */
public interface ChangeRegionFetcher {
    public List<HashSet<String>> getChangeRegion();
    public HashSet<String> getAddedArtifactList();
    public HashSet<String> getRemovedArtifactList();

}
