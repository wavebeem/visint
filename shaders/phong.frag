/*
 * fragment shader template
 */

varying vec3 N;
varying vec3 L;
varying vec3 E;

varying float Y;


// Author: Jenny Orr
void
main() {
    vec3 Normal = normalize(N);
    vec3 Light  = normalize(L);
    vec3 Eye    = normalize(E);
    vec3 Half   = normalize(Eye + Light);

    float f  = 1.0;
    float kd = max(dot(Normal, Light), 0.0);
    float ks = pow(max(dot(Half, Normal), 0.0), 10.0);
    //float ks = pow(max(dot(Half, Normal), 0.0), f * gl_FrontMaterial.shininess);

    if (dot(Normal, Light) < 0.0) {
        f = 0.0;
    }

    vec4 diffuse  = kd *     gl_FrontLightProduct[0].diffuse;
    vec4 specular = ks * f * gl_FrontLightProduct[0].specular;
    vec4 ambient  =          gl_FrontLightProduct[0].ambient;

    // Set the fragment color for example to gray, alpha 1.0
    vec4 light = ambient + diffuse + specular;
    gl_FragColor = light * gl_Color;
}

