package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Ren extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Ren.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.Normal)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(AffinityToken.GetCard(Affinity.Dark), true));
    public static final int THRESHOLD = 3;


    public Ren()
    {
        super(DATA);

        Initialize(0, 2, 3, 2);
        SetAffinity_Dark(1, 0, 1);
        SetAffinity_Blue(1, 0, 0);
        SetEthereal(true);
    }

    @Override
    public void OnUpgrade() {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.Callback(() -> {
           if (GameUtilities.GetOrbCount(Dark.ORB_ID) == 0) {
               GameActions.Bottom.ChannelOrb(new Dark()).AddCallback(() -> DoAction(m));
           }
           else {
               GameActions.Bottom.TriggerOrbPassive(1, false, false).SetFilter(o -> Dark.ORB_ID.equals(o.ID)).AddCallback(() -> DoAction(m));
           }
        });
    }

    protected void DoAction(AbstractMonster m) {
        int darkCount = GameUtilities.GetOrbCount(Dark.ORB_ID);
        GameActions.Bottom.ApplyPoison(TargetHelper.Normal(m), darkCount * magicNumber);

        if (darkCount >= THRESHOLD && CombatStats.TryActivateSemiLimited(cardID)) {
            GameActions.Bottom.ObtainAffinityToken(Affinity.Dark, upgraded);
        }
    }
}