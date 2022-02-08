package pinacolada.cards.fool.series.NoGameNoLife;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class IzunaHatsuse extends FoolCard
{
    public static final PCLCardData DATA = Register(IzunaHatsuse.class)
            .SetAttack(0, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    private boolean transformed;

    public IzunaHatsuse()
    {
        this(false);
    }

    private IzunaHatsuse(boolean transformed)
    {
        super(DATA);

        Initialize(1, 0, 2, 1);
        SetUpgrade(1, 1, 0);

        SetAffinity_Red(1);
        SetAffinity_Green(1, 0, 1);

        SetHitCount(2);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null)
        {
            PCLGameUtilities.GetPCLIntent(m).AddWeak();
            if (CombatStats.CanActivateSemiLimited(cardID) && PCLGameUtilities.GetHealthPercentage(player) < 0.2f) {
                PCLGameUtilities.GetPCLIntent(m).AddStrength(-magicNumber);
            }
        }
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return upgraded ? super.GetBlockInfo() : null;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_DIAGONAL);
        PCLActions.Bottom.ApplyWeak(p, m, 1);
        if (upgraded)
        {
            PCLActions.Bottom.GainBlock(block);
        }

        if (PCLGameUtilities.GetHealthPercentage(player) < 0.2f && info.TryActivateSemiLimited()) {
            PCLActions.Bottom.ApplyShackles(TargetHelper.Normal(m), magicNumber);
        }
    }
}