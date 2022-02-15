package pinacolada.cards.fool.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.actions.orbs.EvokeOrb;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.SFX;
import pinacolada.powers.fool.SilencedPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Yae extends FoolCard
{
    public static final PCLCardData DATA = Register(Yae.class).SetSkill(1, CardRarity.RARE, PCLCardTarget.None).SetColorless().SetMaxCopies(2).SetSeries(CardSeries.HoukaiGakuen);

    public Yae()
    {
        super(DATA);

        Initialize(0, 0, 2, 4);
        SetUpgrade(0, 0, 0);

        SetAffinity_Blue(1);
        SetAffinity_Dark(1);
        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.SFX(SFX.NECRONOMICON,1.5f,1.5f);
        int lightningCount = 0;
        for (AbstractMonster enemy : PCLGameUtilities.GetEnemies(true)) {
            PCLActions.Bottom.StackPower(new SilencedPower(enemy, magicNumber));
        }
        PCLActions.Bottom.EvokeOrb(player.filledOrbCount(), EvokeOrb.Mode.Sequential)
                .SetFilter(o -> Lightning.ORB_ID.equals(o.ID) || (upgraded && Dark.ORB_ID.equals(o.ID)))
                .AddCallback(orbs ->
                {
                    if (orbs.size() == 0)
                    {
                        PCLActions.Bottom.ApplyBlinded(TargetHelper.AllCharacters(), secondaryValue);
                    }
                    else
                    {
                        PCLActions.Bottom.ChannelOrb(new Frost());
                    }
                });
    }
}