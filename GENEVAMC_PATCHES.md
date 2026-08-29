# GenevaMC patch set

## NetherNet signaling health recovery

Upstream's `SessionManagerCore.checkConnection()` checks the Xbox RTA socket
and the local NetherNet server channel. The local server channel remains open
when the separate Xbox signaling channel closes, leaving the broadcaster
listed in the Friends tab while every join hangs.

GenevaMC additionally checks the underlying signaling channel. When it is
closed or absent, the existing session recreation path rebuilds the Xbox
session and NetherNet transport automatically at the next session update.

This addresses the failure documented in upstream issues #383 and #397.

## Matched Bedrock 2169 protocol stack

The upstream 1.26.45 release combined July builds of `common` and
`bedrock-connection` with an August build of `bedrock-codec`. GenevaMC uses the
matched August 28 build 20 release of all three Cloudburst protocol components
to keep NetherNet framing and the Bedrock 2169 codec synchronized.
