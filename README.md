# dependency

```access transformers
        <repository>
            <id>nealxyc-github-repo</id>
            <url>https://raw.githubusercontent.com/nealxyc/mvn-repo/master/</url>
            <releases>
                <enabled>true</enabled>
                <updatePolicy>daily</updatePolicy>
            </releases>
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
            </snapshots>
        </repository>
```

```access transformers
        <dependency>
            <groupId>com.lairon.libs</groupId>
            <artifactId>0xTask</artifactId>
            <version>1.0-SNAPSHOT</version>
            <scope>compile</scope>
        </dependency>
```