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
