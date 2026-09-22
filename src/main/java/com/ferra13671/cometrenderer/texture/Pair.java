package com.ferra13671.cometrenderer.texture;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apiguardian.api.API;

@API(status = API.Status.INTERNAL, since = "3.0")
@Getter
@Setter
@AllArgsConstructor
public class Pair<A, B> {
    private A left;
    private B right;
}
