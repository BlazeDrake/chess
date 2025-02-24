package network.results;

import network.datamodels.GameData;

import java.util.Collection;

public record ListGamesResult(Collection<GameData> games) {
}
